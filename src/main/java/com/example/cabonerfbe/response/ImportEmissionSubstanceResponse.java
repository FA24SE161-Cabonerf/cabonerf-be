package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.MidpointImpactCharacterizationFactorsDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * The class Import emission substance response.
 *
 * @author SonPHH.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportEmissionSubstanceResponse {
    private List<MidpointImpactCharacterizationFactorsDto> dataImportSuccess;
    private Map<String, String> dataImportFailure;
}
