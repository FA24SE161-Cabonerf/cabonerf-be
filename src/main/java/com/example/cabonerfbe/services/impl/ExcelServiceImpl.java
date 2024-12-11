package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointSubstanceFactorsDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.ExportFactorRequest;
import com.example.cabonerfbe.response.ImportFactorResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import com.example.cabonerfbe.services.ExcelService;
import com.example.cabonerfbe.services.MidpointService;
import com.example.cabonerfbe.services.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * The class Excel service.
 *
 * @author SonPHH.
 */
@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ArrayList<String> errorContent = new ArrayList<>();
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
    @Autowired
    private S3Service s3Service;

    @Override
    public ImportFactorResponse readExcel(MultipartFile file, String name) throws IOException {
        List<MidpointSubstanceFactorsResponse> newRecords = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        List<MidpointImpactCharacterizationFactors> factorsList = Collections.synchronizedList(new ArrayList<>());

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            ImpactCategory category = impactCategoryRepository.findByName(workbook.getSheetName(0).toUpperCase());
            if (category == null) {
                throw CustomExceptions.notFound("Impact category not exist");
            }
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
            BigDecimal value = getBigDecimalValueFromCell(row.getCell(col));
            if (value != null) {
                ImpactMethodCategory methodCategory = getImpactMethodCategory(methodName, category.getId(), name);
                if (createOrUpdateFactor(factorsList, emissionSubstance, methodCategory, value, 0, row.getRowNum(), col)) {
                    response.setMethodValue(methodName, value);
                }
            }
        });
        if (response.getEgalitarian() == null && response.getEgalitarian() == null && response.getIndividualist() == null) {
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
            byte[] fileData = s3Service.downloadFile(fileName);

            if (fileData != null && fileData.length > 0) {
                ByteArrayResource resource = new ByteArrayResource(fileData);
                String name = fileName.contains("error") ? "error.xlsx" : "factor-template.xlsx";
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"");


                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(fileData.length)
                        .body(resource);
            } else {
                throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "File not found in S3");
            }
        } catch (RuntimeException e) {
            log.error("Error downloading template from S3: {}", e.getMessage());
            throw e; // rethrow after logging
        }
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

    private BigDecimal getBigDecimalValueFromCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                String stringValue = cell.getStringCellValue();
                try {
                    // Cố gắng chuyển chuỗi về BigDecimal
                    return new BigDecimal(stringValue);
                } catch (NumberFormatException e) {
                    return null;
                }
            case BLANK:
                return null;
            default:
                return null;
        }
    }

//    private boolean isNumeric(String str) {
//        if (str == null || str.isEmpty()) {
//            return false;
//        }
//        try {
//            Double.parseDouble(str);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }

    private boolean createOrUpdateFactor(List<MidpointImpactCharacterizationFactors> factorsList,
                                         EmissionSubstance emissionSubstance, ImpactMethodCategory methodCategory, BigDecimal value,
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
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            // Tạo CellStyle và Font để định dạng màu chữ đỏ
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setColor(IndexedColors.RED.getIndex()); // Đặt màu đỏ
            style.setFont(font);

            for (String e : errors) {
                String[] error = e.split(" - ");
                int sheetIndex = Integer.parseInt(error[0]);
                int rowIndex = Integer.parseInt(error[1]);
                int cellIndex = Integer.parseInt(error[2]);
                String errorMessage = error[3];

                Sheet sheet = workbook.getSheetAt(sheetIndex);
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }
                // Chèn dữ liệu vào ô tương ứng và áp dụng định dạng màu đỏ
                Cell cell = row.createCell(cellIndex);
                cell.setCellValue(errorMessage);
                cell.setCellStyle(style);
            }

            // Ghi workbook vào ByteArrayOutputStream
            workbook.write(byteArrayOutputStream);

            // Tạo tên file dựa trên tên file gốc và thời gian hiện tại
            String originalFileName = file.getOriginalFilename();
            String fileNameWithoutExtension = originalFileName != null && originalFileName.contains(".")
                    ? originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                    : originalFileName;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH_mm_ss");
            String formattedTime = LocalTime.now().format(formatter);
            String s3FileName = fileNameWithoutExtension + "_error_log_" + formattedTime + ".xlsx";

            MultipartFile multipartFile = convertToMultipartFile(byteArrayOutputStream, s3FileName);
            // Gọi hàm updateFile để lưu file lên S3
            return s3Service.uploadError(multipartFile);

        } catch (IOException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi xảy ra
        }

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
                BigDecimal value = Optional.ofNullable(x.getIndividualist())
                        .orElse(Optional.ofNullable(x.getHierarchist())
                                .orElse(Optional.ofNullable(x.getEgalitarian())
                                        .orElse(null)));

                // Chuyển giá trị thành chuỗi và đặt vào cell
                row.createCell(6).setCellValue(value != null ? value.toPlainString() : "");
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

    private MultipartFile convertToMultipartFile(ByteArrayOutputStream byteArrayOutputStream, String fileName) {
        try {
            // Chuyển ByteArrayOutputStream thành InputStream
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            // Tạo MultipartFile từ InputStream
            MultipartFile multipartFile = new MockMultipartFile(
                    fileName, // Tên file
                    fileName, // Tên file gốc
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // Content-Type
                    inputStream // Dữ liệu file
            );

            return multipartFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert ByteArrayOutputStream to MultipartFile", e);
        }
    }
}
