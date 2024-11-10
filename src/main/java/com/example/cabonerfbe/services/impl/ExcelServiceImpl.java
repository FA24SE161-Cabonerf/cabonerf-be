package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointSubstanceFactorsDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.ExportFactorRequest;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.ImportFactorResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import com.example.cabonerfbe.services.ExcelService;
import com.example.cabonerfbe.services.MidpointService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private EmissionCompartmentRepository emissionCompartmentRepository;
    @Autowired
    private SubstanceRepository substanceRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorsRepository repository;
    @Autowired
    private MidpointImpactCharacterizationFactorConverter converter;
    @Autowired
    private ImpactCategoryRepository impactCategoryRepository;
    @Autowired
    private ImpactMethodCategoryRepository impactMethodCategoryRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private EmissionSubstanceRepository emissionSubstanceRepository;
    @Autowired
    private MidpointService midpointService;
    @Autowired
    private MidpointRepository midpointRepository;
    @Autowired
    private MidpointImpactCharacterizationFactorConverter midpointConverter;

    private ArrayList<String> errorContent = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public ImportFactorResponse readExcel(MultipartFile file, String name) throws IOException {
        List<MidpointSubstanceFactorsResponse> newRecords = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        List<MidpointImpactCharacterizationFactors> factorsList = Collections.synchronizedList(new ArrayList<>());

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            ImpactCategory category = impactCategoryRepository.findByName(workbook.getSheetName(0).toUpperCase());

            for (Row row : sheet) {
                if (row.getRowNum() < 5 || shouldSkipRow(row)) continue;

                // Submit each row processing task asynchronously
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        MidpointSubstanceFactorsResponse response = processRow(row, factorsList, category, name);
                        if (response != null) newRecords.add(response);
                    } catch (Exception e) {
                        int columnWithError = getColumnWithError(e, row);
                        errorContent.add("0 - " + row.getRowNum() + " - " + columnWithError + " - Data invalid");
                    }
                }, executorService);
                futures.add(future);
            }

            // Wait for all futures to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            repository.saveAll(factorsList);
        }

        String filePath = errorContent.isEmpty() ? null : logError(file, errorContent);
        return ImportFactorResponse.builder().importData(newRecords).filePath(filePath).build();
    }

    private MidpointSubstanceFactorsResponse processRow(Row row, List<MidpointImpactCharacterizationFactors> factorsList,
                                                        ImpactCategory category, String name) {
        Substance substance = processSubstance(row);
        EmissionCompartment compartment = fetchCompartment(row.getCell(5).getStringCellValue());
        EmissionSubstance emissionSubstance = fetchEmissionSubstance(substance, compartment, category);

        Map<String, Integer> methodColumns = Map.of("Individualist", 6, "Hierarchist", 7, "Egalitarian", 8);
        MidpointSubstanceFactorsResponse response = new MidpointSubstanceFactorsResponse(
                emissionSubstance.getId(), checkCas(row.getCell(0)), substance.getName(),
                substance.getChemicalName(), compartment.getName(), substance.getMolecularFormula(),
                substance.getAlternativeFormula(), null, null, null);

        methodColumns.forEach((methodName, col) -> {
            Double value = getDoubleValueFromCell(row.getCell(col));
            if (value != null) {
                ImpactMethodCategory methodCategory = getImpactMethodCategory(methodName, category.getId(), name);
                if (createOrUpdateFactor(factorsList, emissionSubstance, methodCategory, value,0,row.getRowNum(),col)) {
                    response.setMethodValue(methodName, value);
                }
            }
        });
        if(response.getEgalitarian() == null && response.getEgalitarian() == null && response.getIndividualist() == null){
            return null;
        }
        return response;
    }

    private Substance processSubstance(Row row) {
        String substanceName = row.getCell(1).getStringCellValue().trim();
        return substanceRepository.findByName(substanceName)
                .orElseGet(() -> substanceRepository.save(new Substance(
                        substanceName,
                        row.getCell(2).getStringCellValue().trim(),
                        getStringOrNull(row.getCell(3)),
                        getStringOrNull(row.getCell(4)),
                        checkCas(row.getCell(0))
                )));
    }

    private String getStringOrNull(Cell cell) {
        return (cell != null && cell.getCellType() == CellType.STRING) ? cell.getStringCellValue().trim() : null;
    }


    private EmissionCompartment fetchCompartment(String compartmentName) {
        return compartmentName.isBlank() ? null :
                emissionCompartmentRepository.findByName(compartmentName)
                        .orElseThrow(() -> new IllegalArgumentException("Emission compartment '" + compartmentName + "' not found"));
    }

    private EmissionSubstance fetchEmissionSubstance(Substance substance, EmissionCompartment compartment, ImpactCategory category) {
        return emissionSubstanceRepository.checkExistBySubstanceAndCompartment(substance.getId(), compartment.getId())
                .orElseGet(() -> emissionSubstanceRepository.save(new EmissionSubstance(
                        substance, compartment, findAppropriateUnit(category), compartment.getName().equals("Natural Resource")
                )));
    }

    private String checkCas(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue(); // Lấy giá trị chuỗi
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue()); // Chuyển số thành chuỗi
        } else {
            return "-"; // Xử lý khi ô trống hoặc kiểu khác nếu cần
        }
    }

    @Override
    public ResponseEntity<Resource> downloadErrorLog(String fileName) {
        try {
            String projectRootPath = System.getProperty("user.dir");
            String folderPath = projectRootPath + "/src/main/resources/error";

            Path filePath = Paths.get(folderPath, fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "File not exist");
            }
        } catch (MalformedURLException e) {
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> downloadTemplate() {
        try {
            String projectRootPath = System.getProperty("user.dir");
            String folderPath = projectRootPath + "/src/main/resources/admin-templates/";
            String filename = "factor-template.xlsx";
            Path filePath = Paths.get(folderPath, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "File not exist");
            }
        } catch (MalformedURLException e) {
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> exportFactor(ExportFactorRequest request) {
        ImpactCategory category = impactCategoryRepository.findByIdAndStatus(request.getImpactCategoryId(), true)
                .orElseThrow(() -> CustomExceptions.notFound("Impact category not exist"));

        LifeCycleImpactAssessmentMethod method = methodRepository.findByIdAndStatus(request.getMethodId(), true)
                .orElseThrow(() -> CustomExceptions.notFound("Method not exist"));

        ImpactMethodCategory imc = impactMethodCategoryRepository.findByMethodAndCategory(request.getImpactCategoryId(), request.getMethodId())
                .orElseThrow(() -> CustomExceptions.notFound("Not exist category with method"));

        List<MidpointSubstanceFactorsDto> dataList = midpointRepository.findAllToExport(imc.getId()).stream()
                .map(midpointConverter::fromQueryResultsToDto)
                .collect(Collectors.toList());

        byte[] excel = writeDataToExcel(dataList, category.getName());

        // Tạo ByteArrayResource từ dữ liệu excel
        ByteArrayResource resource = new ByteArrayResource(excel);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + method.getName() + " (" + method.getPerspective().getAbbr() + ").xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(excel.length)
                .body(resource);
    }


    private ImpactMethodCategory getImpactMethodCategory(String methodName,
                                                         UUID categoryId, String lifeCycleImpactAssessmentMethodName) {
        LifeCycleImpactAssessmentMethod method = methodRepository.findByName(methodName, lifeCycleImpactAssessmentMethodName);
        return impactMethodCategoryRepository.findByImpactCategoryAndImpactMethod(categoryId, method.getId());
    }

    private Double getDoubleValueFromCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                String stringValue = cell.getStringCellValue();
                try {
                    // Cố gắng chuyển chuỗi về double
                    return Double.parseDouble(stringValue);
                } catch (NumberFormatException e) {
                    return null;
                }
            case BLANK:
                return null;
            default:
                return null;
        }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean createOrUpdateFactor(List<MidpointImpactCharacterizationFactors> factorsList,
                                         EmissionSubstance emissionSubstance, ImpactMethodCategory methodCategory, Double value,
                                         int sheetNumber, int rowsNumber, int columnsNumber) {
        if (repository.checkExist(emissionSubstance.getId(), methodCategory.getId()).isEmpty()) {
            factorsList.add(new MidpointImpactCharacterizationFactors(methodCategory, emissionSubstance,
                    String.format("%.2e", value), value));
            return true;
        }
        errorContent.add(sheetNumber + " - " + rowsNumber + " - " + columnsNumber + " - Data already exists");
        return false;
    }


    private Unit findAppropriateUnit(ImpactCategory category) {
        String unitName = category.getMidpointImpactCategory().getUnit().getName().split(" ")[0];

        return unitRepository.findByUnitGroup(unitName);
    }

    private int getColumnWithError(Exception e, Row row) {
        for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
            try {
                row.getCell(columnIndex).getStringCellValue(); // Cố gắng đọc giá trị của ô
            } catch (Exception cellException) {
                return columnIndex; // Trả về vị trí cột gặp lỗi
            }
        }
        return -1;
    }

    private String logError(MultipartFile file, ArrayList<String> errors) {
        String projectRootPath = System.getProperty("user.dir");
        String folderPath = projectRootPath + "/src/main/resources/error";
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // Tạo CellStyle và Font để định dạng màu chữ đỏ
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setColor(IndexedColors.RED.getIndex()); // Đặt màu đỏ
            style.setFont(font);

            for (String e : errors) {
                String[] error = e.split(" - ");
                Sheet sheet = workbook.getSheetAt(Integer.parseInt(error[0]));

                Row row = sheet.getRow(Integer.parseInt(error[1]));
                if (row == null) {
                    row = sheet.createRow(Integer.parseInt(error[1]));
                }

                // Chèn dữ liệu vào ô tương ứng và áp dụng định dạng màu đỏ
                Cell cell = row.createCell(Integer.parseInt(error[2]));
                cell.setCellValue(error[3]);
                cell.setCellStyle(style);
            }
            String originalFileName = file.getOriginalFilename();
            String fileNameWithoutExtension = originalFileName != null && originalFileName.contains(".")
                    ? originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                    : originalFileName;
            // Đường dẫn file đầu ra

            LocalTime currentDate = LocalTime.now();

            // Định dạng ngày theo định dạng dd_MM_yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH_mm_ss");
            String formattedDate = currentDate.format(formatter);

            String outputFilePath = fileNameWithoutExtension + "_error_log_" + formattedDate + ".xlsx";

            // Ghi lại tệp Excel vào đường dẫn chỉ định
            try (FileOutputStream outputStream = new FileOutputStream(folderPath + "/" + outputFilePath)) {
                workbook.write(outputStream);
            }
            return outputFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean shouldSkipRow(Row row) {
        if (row == null || row.getRowNum() < 5) {
            return true;
        }

        // Kiểm tra xem row có dữ liệu không
        boolean hasData = false;
        for (int i = 0; i <= 8; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !isCellEmpty(cell)) {
                hasData = true;
                break;
            }
        }
        return !hasData;
    }

    private boolean isCellEmpty(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim().isEmpty();
            case NUMERIC:
                return false;
            case BLANK:
                return true;
            default:
                return true;
        }
    }

    private byte[] writeDataToExcel(List<MidpointSubstanceFactorsDto> data, String categoryName) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(categoryName);

            // Tạo tiêu đề cột
            String[] headers = {"CAS", "Name", "Chemical Name", "Compartment Name",
                    "Molecular Formula", "Alternative Formula", "Individualist",
                    "Hierarchist", "Egalitarian"};
            Row headerRow = sheet.createRow(4);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Ghi dữ liệu vào các dòng tiếp theo
            int rowNum = 5;
            for (MidpointSubstanceFactorsDto x : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(x.getCasNumber());
                row.createCell(1).setCellValue(x.getName());
                row.createCell(2).setCellValue(x.getChemicalName());
                row.createCell(3).setCellValue(x.getCompartmentName());
                row.createCell(4).setCellValue(x.getMolecularFormula());
                row.createCell(5).setCellValue(x.getAlternativeFormula());
                row.createCell(6).setCellValue(x.getIndividualist() != null ? x.getIndividualist() : '-');
                row.createCell(7).setCellValue(x.getHierarchist() != null ? x.getHierarchist() : '-');
                row.createCell(8).setCellValue(x.getEgalitarian() != null ? x.getEgalitarian() : '-');
            }

            // Ghi dữ liệu vào file
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @PreDestroy
    public void shutdownExecutor() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) executorService.shutdownNow();
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
