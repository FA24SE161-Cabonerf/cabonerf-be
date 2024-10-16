package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MidpointImpactCharacterizationFactorsDto {
    private long id;
    private ImpactMethodCategoryDto impactMethodCategory;
    private EmissionSubstancesDto emissionSubstances;
    private String scientificValue;
    private double decimalValue;
    private UnitDto unit;
    private EmissionCompartmentDto emissionCompartment;
}
