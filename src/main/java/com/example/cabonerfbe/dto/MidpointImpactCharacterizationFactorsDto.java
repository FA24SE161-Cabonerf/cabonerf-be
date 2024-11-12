package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MidpointImpactCharacterizationFactorsDto {
    private UUID id;
    private ImpactMethodCategoryDto impactMethodCategory;
    private SubstanceDto substance;
    private String scientificValue;
    private BigDecimal decimalValue;
    private UnitDto unit;
    private EmissionCompartmentDto emissionCompartment;
}
