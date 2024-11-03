package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.PaginationRequest;
import com.example.cabonerfbe.response.ImportEmissionSubstanceResponse;
import com.example.cabonerfbe.response.ImportFactorResponse;
import com.example.cabonerfbe.response.MidpointSubstanceFactorsResponse;
import com.example.cabonerfbe.services.ExcelService;
import com.example.cabonerfbe.services.MidpointService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private EmissionCompartmentRepository emissionCompartmentRepository;
    @Autowired
    private EmissionSubstancesRepository emissionSubstancesRepository;
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
    private SubstancesCompartmentsRepository substancesCompartmentsRepository;
    @Autowired
    private MidpointService midpointService;

    private ArrayList<String> errorContent = new ArrayList<>();

    @Override
    public ImportFactorResponse readExcel(MultipartFile file, String name) throws IOException {
        List<MidpointSubstanceFactorsResponse> responses = new ArrayList<>();
        List<MidpointSubstanceFactorsResponse> newRecords = new ArrayList<>(); // Danh sách cho các bản ghi mới
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            List<MidpointImpactCharacterizationFactors> list = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                ImpactCategory category = impactCategoryRepository.findByName(workbook.getSheetName(i).toUpperCase());
                for (Row row : sheet) {
                    if (row.getRowNum() < 5) {
                        continue; // Skip header
                    }
                    try {
                        String cas = row.getCell(0).getStringCellValue();
                        String substanceName = row.getCell(1).getStringCellValue();
                        String chemicalName = row.getCell(2).getStringCellValue();
                        String molecularFormula = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null;
                        String alternativeFormula = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : null;
                        String compartmentName = row.getCell(5).getStringCellValue();
                        Double individualist = getDoubleValueFromCell(row.getCell(6));
                        Double hierarchist = getDoubleValueFromCell(row.getCell(7));
                        Double egalitarian = getDoubleValueFromCell(row.getCell(8));

                        // Kiểm tra từng giá trị null và ghi log lỗi
                        if (individualist == null) {
                            errorContent.add(i + " - " + row.getRowNum() + " - 6 - Data invalid");
                        }
                        if (hierarchist == null) {
                            errorContent.add(i + " - " + row.getRowNum() + " - 7 - Data invalid");
                        }
                        if (egalitarian == null) {
                            errorContent.add(i + " - " + row.getRowNum() + " - 8 - Data invalid");
                        }

                        // Create or find emission substance
                        EmissionSubstances emissionSubstance = emissionSubstancesRepository.findByName(substanceName)
                                .orElseGet(() -> {
                                    EmissionSubstances newSubstance = new EmissionSubstances();
                                    newSubstance.setName(substanceName);
                                    newSubstance.setChemicalName(chemicalName);
                                    newSubstance.setMolecularFormula(molecularFormula);
                                    newSubstance.setAlternativeFormula(alternativeFormula);
                                    return emissionSubstancesRepository.save(newSubstance);
                                });

                        // Find emission compartment
                        EmissionCompartment emissionCompartment = emissionCompartmentRepository.findByName(compartmentName)
                                .orElseThrow(() -> new IllegalArgumentException("Emission compartment '" + compartmentName + "' not found"));

                        // Create or find substance compartment
                        SubstancesCompartments substanceCompartment = substancesCompartmentsRepository.checkExist(emissionSubstance.getId(), emissionCompartment.getId())
                                .orElseGet(() -> {
                                    SubstancesCompartments newSubstancesCompartments = new SubstancesCompartments();
                                    newSubstancesCompartments.setEmissionSubstance(emissionSubstance);
                                    newSubstancesCompartments.setEmissionCompartment(emissionCompartment);
                                    return substancesCompartmentsRepository.save(newSubstancesCompartments);
                                });



                        ImpactMethodCategory _individualist = getImpactMethodCategory("Individualist", category.getId(), name);
                        ImpactMethodCategory _hierarchist = getImpactMethodCategory("Hierarchist", category.getId(), name);
                        ImpactMethodCategory _egalitarian = getImpactMethodCategory("Egalitarian", category.getId(), name);


                        MidpointSubstanceFactorsResponse response = new MidpointSubstanceFactorsResponse(
                                substanceCompartment.getId(), cas, emissionSubstance.getName(),
                                emissionSubstance.getChemicalName(), emissionCompartment.getName(),
                                emissionSubstance.getMolecularFormula(), emissionSubstance.getAlternativeFormula(),
                                null, null, null
                        );

                        // Tạo hoặc cập nhật giá trị cho từng phương pháp đánh giá
                        if (isNumeric(row.getCell(6).toString())) {
                            if (createOrUpdateFactor(list, substanceCompartment, _individualist, cas, individualist, i, row.getRowNum(), 6)) {
                                response.setIndividualist(individualist);
                                newRecords.add(response); // Chỉ thêm vào nếu là bản ghi mới
                            }
                        }
                        if (isNumeric(row.getCell(7).toString())) {
                            if (createOrUpdateFactor(list, substanceCompartment, _hierarchist, cas, hierarchist, i, row.getRowNum(), 7)) {
                                response.setHierarchist(hierarchist);
                                newRecords.add(response);
                            }
                        }
                        if (isNumeric(row.getCell(8).toString())) {
                            if (createOrUpdateFactor(list, substanceCompartment, _egalitarian, cas, egalitarian, i, row.getRowNum(), 8)) {
                                response.setEgalitarian(egalitarian);
                                newRecords.add(response);
                            }
                        }
                    } catch (Exception rowException) {
                        int columns = getColumnWithError(rowException, row);
                        errorContent.add(i + " - " + row.getRowNum() + " - " + (columns - 1) + " - Data invalid");
                    }
                }
            }
            repository.saveAll(list);
            workbook.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        PaginationRequest request = new PaginationRequest();
        request.setCurrentPage(1);
        request.setPageSize(20);
        String filePath = logError(file, errorContent);

        return ImportFactorResponse.builder()
                .importData(newRecords)
                .filePath(filePath)
                .build();
    }

    @Override
    public ResponseEntity<Resource> downloadErrorLog(String fileName) {
        try {
            String projectRootPath = System.getProperty("user.dir");
            String folderPath = projectRootPath + "/src/main/resources/error";

            Path filePath = Paths.get(folderPath, fileName);
            Resource resource = new UrlResource(filePath.toUri());

            // Kiểm tra xem file có tồn tại và có thể đọc được không
            if (resource.exists() && resource.isReadable()) {
                // Đặt headers để tải xuống file
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
                                         SubstancesCompartments substanceCompartment,
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
                            factor.setSubstancesCompartments(substanceCompartment);
                            factor.setDecimalValue(value);
                            factor.setScientificValue(String.format("%.2e", value));
                            factor.setCas(cas);
                            factor.setImpactMethodCategory(methodCategory);
                            factor.setUnit(findAppropriateUnit(methodCategory));

                            // Add to the list and update isNewRecord
                            list.add(factor);
                            isNewRecord.set(true);
                        });

        return isNewRecord.get();
    }




    private Unit findAppropriateUnit(ImpactMethodCategory methodCategory) {
        List<Unit> units = unitRepository.findByName("-eq");
        for (Unit unit : units) {
            if (unit.getName().contains(methodCategory.getImpactCategory().getMidpointImpactCategory().getUnit().getName())) {
                return unit;
            }
        }
        return null; // or handle appropriately if no matching unit is found
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
            String outputFilePath = fileNameWithoutExtension + "_errorLog.xlsx";

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
}
