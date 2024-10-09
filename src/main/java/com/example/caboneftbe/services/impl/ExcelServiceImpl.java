package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.MidpointImpactCharacterizationFactorConverter;
import com.example.caboneftbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.caboneftbe.models.EmissionCompartment;
import com.example.caboneftbe.models.EmissionSubstances;
import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;
import com.example.caboneftbe.repositories.EmissionCompartmentRepository;
import com.example.caboneftbe.repositories.EmissionSubstancesRepository;
import com.example.caboneftbe.repositories.MidpointImpactCharacterizationFactorsRepository;
import com.example.caboneftbe.response.ImportEmissionSubstanceResponse;
import com.example.caboneftbe.services.ExcelService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    @Override
    public ImportEmissionSubstanceResponse readExcel(MultipartFile file) {
        List<MidpointImpactCharacterizationFactorsDto> list = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // Duyệt qua từng sheet trong workbook
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = workbook.getSheetName(i); // Lấy tên sheet hiện tại
                Iterator<Row> rows = sheet.iterator();

                while (rows.hasNext()) {
                    Row currentRow = rows.next();

                    if (currentRow.getRowNum() < 5) {
                        // Bỏ qua hàng tiêu đề
                        continue;
                    }

                    try {
                        for(int j = 6; j < 9;j++){

                        MidpointImpactCharacterizationFactors data = new MidpointImpactCharacterizationFactors();
                        EmissionSubstances emissionSubstances;

                        data.setCas(currentRow.getCell(0).getStringCellValue());

                        emissionSubstances = emissionSubstancesRepository.findByName(currentRow.getCell(1).getStringCellValue());
                        if(emissionSubstances == null){
                            emissionSubstances = new EmissionSubstances();
                            emissionSubstances.setName(currentRow.getCell(1).getStringCellValue());
                            emissionSubstances.setChemicalName(currentRow.getCell(2).getStringCellValue());
                            emissionSubstances.setMolecularFormula(currentRow.getCell(3).getStringCellValue());
                            emissionSubstances.setAlternativeFormula(currentRow.getCell(4).getStringCellValue());

                            emissionSubstances = emissionSubstancesRepository.save(emissionSubstances);
                        }
                        data.setEmissionSubstances(emissionSubstances);
                        EmissionCompartment emissionCompartment = emissionCompartmentRepository.findByName(currentRow.getCell(5).getStringCellValue());
                        data.setEmissionCompartment(emissionCompartment);


                            MidpointImpactCharacterizationFactors factors = new MidpointImpactCharacterizationFactors();
                            factors = insertData(data, currentRow.getCell(j).getNumericCellValue());
                            list.add(converter.INSTANCE.fromMidpointImpactCharacterizationFactorsToMidpointImpactCharacterizationFactorsDto(factors));
                        }

                    } catch (Exception e) {
                        // In ra lỗi, tên sheet và dòng hiện tại
                        System.err.println("Lỗi tại sheet: " + sheetName + ", dòng: " + (currentRow.getRowNum() + 1) + " - " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ImportEmissionSubstanceResponse.builder()
                .list(list)
                .build();
    }

    private MidpointImpactCharacterizationFactors insertData(MidpointImpactCharacterizationFactors factors, double value) {
        if (value == 0) {
            factors.setDecimalValue(0);
            factors.setScientificValue("0");
        } else {
            factors.setDecimalValue(value);
            String scientific_value = String.format("%.2e", value);
            factors.setScientificValue(scientific_value);
        }
        factors = repository.save(factors);
        return factors;
    }


}
