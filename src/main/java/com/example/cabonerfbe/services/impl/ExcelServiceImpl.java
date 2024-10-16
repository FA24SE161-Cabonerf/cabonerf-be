package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.response.ImportEmissionSubstanceResponse;
import com.example.cabonerfbe.services.ExcelService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    private LifeCycleImpactAssessmentMethodRepository lifeCycleImpactAssessmentMethodRepository;
    @Autowired
    private UnitRepository unitRepository;

    private Map<String, String> error = new HashMap<>();

    @Override
    public ImportEmissionSubstanceResponse readExcel(MultipartFile file, String name) {

        if(lifeCycleImpactAssessmentMethodRepository.findAllByName(name).isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, Map.of("request", "Life Cycle Impact Assessment Method not found"));
        }

        List<MidpointImpactCharacterizationFactorsDto> list = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // Duyệt qua từng sheet trong workbook
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = workbook.getSheetName(i); // Lấy tên sheet hiện tại
                Iterator<Row> rows = sheet.iterator();
                String categoryName = "";
                ImpactCategory category = new ImpactCategory();
                category = impactCategoryRepository.findByName(sheetName.toUpperCase());

                if (category == null) {
                    error.put("Sheet excel with name: " + sheetName, "Impact Category not exist");
                    continue;
                }
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    if (currentRow.getRowNum() < 5) {
                        // Bỏ qua hàng tiêu đề
                        continue;
                    }

                    list.add(insertData(currentRow, 6, category.getId(), name, sheetName));
                    list.add(insertData(currentRow, 7, category.getId(), name, sheetName));
                    list.add(insertData(currentRow, 8, category.getId(), name, sheetName));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        list.removeIf(Objects::isNull);
        return ImportEmissionSubstanceResponse.builder()
                .dataImportSuccess(list)
                .dataImportFailure(error)
                .build();
    }

    private MidpointImpactCharacterizationFactorsDto insertData(
            Row currentRow,
            int rowValue,
            long categoryId, String lifeCycleImpactAssessmentMethodName, String sheetName) {
        try {
            MidpointImpactCharacterizationFactors data = new MidpointImpactCharacterizationFactors();
            EmissionSubstances emissionSubstances;

            switch (rowValue) {
                case 6:
                    data.setImpactMethodCategory(getImpactMethodCategory("Individualist", categoryId, lifeCycleImpactAssessmentMethodName));
                    break;
                case 7:
                    data.setImpactMethodCategory(getImpactMethodCategory("Hierarchist", categoryId, lifeCycleImpactAssessmentMethodName));
                    break;
                case 8:
                    data.setImpactMethodCategory(getImpactMethodCategory("Egalitarian", categoryId, lifeCycleImpactAssessmentMethodName));
                    break;

            }
            data.setCas(currentRow.getCell(0).getStringCellValue());
            emissionSubstances = emissionSubstancesRepository.findByName(currentRow.getCell(1).getStringCellValue(),currentRow.getCell(3).getStringCellValue());
            if (emissionSubstances == null) {
                emissionSubstances = new EmissionSubstances();
                emissionSubstances.setName(currentRow.getCell(1).getStringCellValue());
                emissionSubstances.setChemicalName(currentRow.getCell(2).getStringCellValue());
                emissionSubstances.setMolecularFormula(currentRow.getCell(3).getStringCellValue());
                emissionSubstances.setAlternativeFormula(currentRow.getCell(4).getStringCellValue());

                if (emissionSubstancesRepository.findByName(emissionSubstances.getName(),currentRow.getCell(3).getStringCellValue()) == null) {
                    emissionSubstancesRepository.save(emissionSubstances);
                } else {
                    emissionSubstances = emissionSubstancesRepository.findByName(emissionSubstances.getName(),currentRow.getCell(3).getStringCellValue());
                }
            }
            List<Unit> units = unitRepository.findByName("-eq");
            for(Unit unit : units) {
                if(unit.getName().contains(data.getImpactMethodCategory().getImpactCategory().getMidpointImpactCategory().getUnit().getName())){
                    data.setUnit(unit);
                }
            }
            data.setEmissionSubstances(emissionSubstances);
            EmissionCompartment emissionCompartment = emissionCompartmentRepository.findByName(currentRow.getCell(5).getStringCellValue());
            data.setEmissionCompartment(emissionCompartment);
            if (currentRow.getCell(rowValue).getNumericCellValue() == 0) {
                data.setDecimalValue(0);
                data.setScientificValue("0");
            } else {
                data.setDecimalValue(currentRow.getCell(rowValue).getNumericCellValue());
                String scientific_value = String.format("%.2e", currentRow.getCell(rowValue).getNumericCellValue());
                data.setScientificValue(scientific_value);
            }
            MidpointImpactCharacterizationFactors exist = repository.findByImpactMethodCategoryIdAndEmissionSubstancesName(data.getImpactMethodCategory().getId(), data.getEmissionSubstances().getName(), data.getEmissionCompartment().getId(), data.getEmissionSubstances().getMolecularFormula());
            if (exist != null) {
                if (exist.getDecimalValue() == data.getDecimalValue()) {
                    error.put(sheetName, "Row: " + (currentRow.getRowNum() + 1) + " - Column: " + (rowValue + 1) + " - Midpoint Impact Characterization Factors already exist");
                    return null;
                }
            }
            data = repository.save(data);
            return converter.INSTANCE.fromMidpointToMidpointDto(data);

        } catch (Exception e) {
            error.put(sheetName, "Row: " + (currentRow.getRowNum() + 1) + " - Column: " + (rowValue + 1) + " - " + e.getMessage());
        }
        return null;
    }

    private ImpactMethodCategory getImpactMethodCategory(String methodName, long categoryId, String lifeCycleImpactAssessmentMethodName) {
        LifeCycleImpactAssessmentMethod method = lifeCycleImpactAssessmentMethodRepository.findByName(methodName, lifeCycleImpactAssessmentMethodName);
        return impactMethodCategoryRepository.findByImpactCategoryAndImpactMethod(categoryId, method.getId());
    }
}
