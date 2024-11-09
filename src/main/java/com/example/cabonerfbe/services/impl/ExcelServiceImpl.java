package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.ImportFactorResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import com.example.cabonerfbe.services.ExcelService;
import com.example.cabonerfbe.services.MidpointService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private ArrayList<String> errorContent = new ArrayList<>();

    @Override
    public ImportFactorResponse readExcel(MultipartFile file, String name) throws IOException {
        List<MidpointSubstanceFactorsResponse> responses = new ArrayList<>();
        List<MidpointSubstanceFactorsResponse> newRecords = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
            ImpactCategory category = impactCategoryRepository.findByName(workbook.getSheetName(0).toUpperCase());

            List<MidpointImpactCharacterizationFactors> list = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() < 5) {
                    continue; // Bỏ qua header
                }
                if(shouldSkipRow(row)){
                    continue;
                }
                try {
                    String cas = checkCas(row.getCell(0));
                    String substanceName = row.getCell(1).getStringCellValue().trim();
                    String chemicalName = row.getCell(2).getStringCellValue().trim();
                    String molecularFormula = row.getCell(3) != null ? row.getCell(3).getStringCellValue().trim() : null;
                    String alternativeFormula = row.getCell(4) != null ? row.getCell(4).getStringCellValue().trim() : null;
                    String compartmentName = row.getCell(5).getStringCellValue().trim();
                    Double individualist = getDoubleValueFromCell(row.getCell(6));
                    Double hierarchist = getDoubleValueFromCell(row.getCell(7));
                    Double egalitarian = getDoubleValueFromCell(row.getCell(8));

                    if (individualist == null) {
                        errorContent.add("0 - " + row.getRowNum() + " - 6 - Data invalid");
                    }
                    if (hierarchist == null) {
                        errorContent.add("0 - " + row.getRowNum() + " - 7 - Data invalid");
                    }
                    if (egalitarian == null) {
                        errorContent.add("0 - " + row.getRowNum() + " - 8 - Data invalid");
                    }

                    // Tạo hoặc tìm emission substance
                    Substance substance = substanceRepository.findByName(substanceName)
                            .orElseGet(() -> {
                                Substance newSubstance = new Substance();
                                newSubstance.setName(substanceName);
                                newSubstance.setChemicalName(chemicalName);
                                newSubstance.setMolecularFormula(molecularFormula);
                                newSubstance.setAlternativeFormula(alternativeFormula);
                                newSubstance.setCas(!cas.equals("0.0") ? cas : "-");
                                return substanceRepository.save(newSubstance);
                            });

                    EmissionCompartment emissionCompartment = null;
                    if (!compartmentName.isBlank() || !compartmentName.isEmpty()) {
                        emissionCompartment = emissionCompartmentRepository.findByName(compartmentName)
                                .orElseThrow(() -> new IllegalArgumentException("Emission compartment '" + compartmentName + "' not found"));
                    }

                    EmissionCompartment finalEmissionCompartment = emissionCompartment;
                    EmissionSubstance emissionSubstance = new EmissionSubstance();

                    if (!compartmentName.isBlank() || !compartmentName.isEmpty()) {
                        emissionSubstance = emissionSubstanceRepository.checkExistBySubstanceAndCompartment(
                                substance.getId(),
                                emissionCompartment.getId()
                        ).orElseGet(() -> {
                            EmissionSubstance newEmissionSubstance = new EmissionSubstance();
                            newEmissionSubstance.setSubstance(substance);
                            newEmissionSubstance.setEmissionCompartment(finalEmissionCompartment);
                            newEmissionSubstance.setUnit(findAppropriateUnit(category));
                            newEmissionSubstance.setIsInput(compartmentName.equals("Natural Resource"));
                            return emissionSubstanceRepository.save(newEmissionSubstance);
                        });
                    } else {
                        errorContent.add("0 - " + row.getRowNum() + " - 5 - Compartment is null");
                        continue;
                    }

                    ImpactMethodCategory _individualist = getImpactMethodCategory("Individualist", category.getId(), name);
                    ImpactMethodCategory _hierarchist = getImpactMethodCategory("Hierarchist", category.getId(), name);
                    ImpactMethodCategory _egalitarian = getImpactMethodCategory("Egalitarian", category.getId(), name);

                    MidpointSubstanceFactorsResponse response = new MidpointSubstanceFactorsResponse(
                            emissionSubstance.getId(),
                            !cas.equals("0.0") ? cas : "-",
                            substance.getName(),
                            substance.getChemicalName(),
                            emissionCompartment.getName(),
                            substance.getMolecularFormula(),
                            substance.getAlternativeFormula(),
                            null,
                            null,
                            null
                    );

                    if (isNumeric(row.getCell(6).toString())) {
                        if (createOrUpdateFactor(list, emissionSubstance, _individualist, cas, individualist, 0, row.getRowNum(), 6)) {
                            response.setIndividualist(individualist);
                        }
                    }
                    if (isNumeric(row.getCell(7).toString())) {
                        if (createOrUpdateFactor(list, emissionSubstance, _hierarchist, cas, hierarchist, 0, row.getRowNum(), 7)) {
                            response.setHierarchist(hierarchist);
                        }
                    }
                    if (isNumeric(row.getCell(8).toString())) {
                        if (createOrUpdateFactor(list, emissionSubstance, _egalitarian, cas, egalitarian, 0, row.getRowNum(), 8)) {
                            response.setEgalitarian(egalitarian);
                        }
                    }
                    if(response.getIndividualist() != null && response.getHierarchist() != null && response.getEgalitarian() != null){
                        newRecords.add(response);
                    }
                } catch (Exception rowException) {
                    int columns = getColumnWithError(rowException, row);
                    errorContent.add("0 - " + row.getRowNum() + " - " + (columns - 1) + " - Data invalid");
                }
            }
            repository.saveAll(list);
            workbook.close();
        } catch (Exception ignored) {
        }

        PaginationRequest request = new PaginationRequest();
        request.setCurrentPage(1);
        request.setPageSize(20);
        String filePath = null;
        if (!errorContent.isEmpty()) {
            filePath = logError(file, errorContent);
        }
        return ImportFactorResponse.builder()
                .importData(newRecords)
                .filePath(filePath)
                .build();
    }

    private String checkCas(Cell cell){
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue(); // Lấy giá trị chuỗi
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue()); // Chuyển số thành chuỗi
        } else {
            return  "-"; // Xử lý khi ô trống hoặc kiểu khác nếu cần
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

    private boolean createOrUpdateFactor(List<MidpointImpactCharacterizationFactors> list,
                                         EmissionSubstance substanceCompartment,
                                         ImpactMethodCategory methodCategory,
                                         String cas,
                                         Double value, int sheetNumber, int rowsNumber, int columnsNumber) {
        AtomicBoolean isNewRecord = new AtomicBoolean(false);

        repository.checkExist(substanceCompartment.getId(), methodCategory.getId())
                .ifPresentOrElse(
                        existingFactor -> errorContent.add(sheetNumber + " - " + rowsNumber + " - " + columnsNumber + " - Data already exists"),
                        () -> {
                            // Create a new factor if it doesn't exist
                            MidpointImpactCharacterizationFactors factor = new MidpointImpactCharacterizationFactors();
                            factor.setEmissionSubstance(substanceCompartment);
                            factor.setDecimalValue(value);
                            factor.setScientificValue(String.format("%.2e", value));
                            factor.setImpactMethodCategory(methodCategory);

                            // Add to the list and update isNewRecord
                            list.add(factor);
                            isNewRecord.set(true);
                        });

        return isNewRecord.get();
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
                System.out.println(e);
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

            String outputFilePath = fileNameWithoutExtension + "_error_log_" + formattedDate +".xlsx";

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
}
