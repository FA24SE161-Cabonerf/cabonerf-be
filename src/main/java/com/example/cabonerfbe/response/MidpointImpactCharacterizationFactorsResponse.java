package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.dto.EmissionSubstancesDto;
import com.example.cabonerfbe.dto.ImpactMethodCategoryDto;
import com.example.cabonerfbe.dto.UnitDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MidpointImpactCharacterizationFactorsResponse {
    private long id;
    private ImpactMethodCategoryDto impactMethodCategory;
    private EmissionSubstancesDto emissionSubstances;
    private String scientificValue;
    private double decimalValue;
    private UnitDto unit;
    private EmissionCompartmentDto emissionCompartment;
}
