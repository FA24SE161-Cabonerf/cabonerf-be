package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.cabonerfbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.response.ImportEmissionSubstanceResponse;
import com.example.cabonerfbe.services.ExcelService;
import org.apache.poi.ss.usermodel.*;
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
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private SubstancesCompartmentsRepository substancesCompartmentsRepository;

    private Map<String, String> error = new HashMap<>();

    @Override
    public void readExcel(MultipartFile file, String name) throws IOException {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            List<MidpointImpactCharacterizationFactors> list = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                ImpactCategory category = new ImpactCategory();
                category = impactCategoryRepository.findByName(workbook.getSheetName(i).toUpperCase());
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

                        // Create substance compartment
                        SubstancesCompartments substanceCompartment = new SubstancesCompartments();
                        substanceCompartment.setEmissionSubstance(emissionSubstance);
                        substanceCompartment.setEmissionCompartment(emissionCompartment);
                        substancesCompartmentsRepository.save(substanceCompartment);


                        MidpointImpactCharacterizationFactors factor = new MidpointImpactCharacterizationFactors();
                        MidpointImpactCharacterizationFactors factorIndividualist = new MidpointImpactCharacterizationFactors();
                        factorIndividualist.setSubstancesCompartments(substanceCompartment);
                        factorIndividualist.setDecimalValue(individualist);
                        String scientific_value_individualist = String.format("%.2e", individualist);
                        factorIndividualist.setScientificValue(scientific_value_individualist);
                        factorIndividualist.setCas(cas);
                        factorIndividualist.setImpactMethodCategory(getImpactMethodCategory("Individualist", category.getId(), name));
                        List<Unit> units = unitRepository.findByName("-eq");
                        for (Unit unit : units) {
                            if (unit.getName().contains(factorIndividualist.getImpactMethodCategory().getImpactCategory().getMidpointImpactCategory().getUnit().getName())) {
                                factorIndividualist.setUnit(unit);
                            }
                        }
                        list.add(factorIndividualist);

                        MidpointImpactCharacterizationFactors factorHierarchist = new MidpointImpactCharacterizationFactors();
                        factorHierarchist.setSubstancesCompartments(substanceCompartment);
                        factorHierarchist.setDecimalValue(hierarchist);
                        String scientific_value_hierarchist = String.format("%.2e", hierarchist);
                        factorHierarchist.setScientificValue(scientific_value_hierarchist);
                        factorHierarchist.setCas(cas);
                        factorHierarchist.setImpactMethodCategory(getImpactMethodCategory("Hierarchist", category.getId(), name));
                        list.add(factorHierarchist);

                        MidpointImpactCharacterizationFactors factorEgalitarian = new MidpointImpactCharacterizationFactors();
                        factorEgalitarian.setSubstancesCompartments(substanceCompartment);
                        factorEgalitarian.setDecimalValue(egalitarian);
                        String scientific_value_egalitarian = String.format("%.2e", egalitarian);
                        factorEgalitarian.setScientificValue(scientific_value_egalitarian);
                        factorEgalitarian.setCas(cas);
                        factorEgalitarian.setImpactMethodCategory(getImpactMethodCategory("Egalitarian", category.getId(), name));
                        list.add(factorEgalitarian);

                    } catch (Exception rowException) {
                    }
                }

            }
            repository.saveAll(list);
            workbook.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private ImpactMethodCategory getImpactMethodCategory(String methodName, long categoryId, String lifeCycleImpactAssessmentMethodName) {
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

}
