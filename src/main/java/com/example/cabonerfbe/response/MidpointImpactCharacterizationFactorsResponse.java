package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.dto.ImpactMethodCategoryDto;
import com.example.cabonerfbe.dto.SubstanceDto;
import com.example.cabonerfbe.dto.UnitDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MidpointImpactCharacterizationFactorsResponse {
    private UUID id;
    private ImpactMethodCategoryDto impactMethodCategory;
    private SubstanceDto emissionSubstances;
    private String scientificValue;
    private BigDecimal decimalValue;
    private UnitDto unit;
    private EmissionCompartmentDto emissionCompartment;
}
