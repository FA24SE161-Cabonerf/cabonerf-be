package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SubstancesCompartmentsDto {
    private UUID id;
    private SubstanceDto emissionSubstance;
    private EmissionCompartmentDto emissionCompartment;
    private UnitProjectImpactDto unit;
}
