package com.example.caboneftbe.response;

import com.example.caboneftbe.dto.MidpointImpactCharacterizationFactorsDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportEmissionSubstanceResponse {
    private List<MidpointImpactCharacterizationFactorsDto> dataImportSuccess;
    private Map<String, String> dataImportFailure;
}
