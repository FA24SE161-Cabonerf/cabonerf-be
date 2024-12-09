package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class EmissionSubstanceDto {
    private UUID id;
    private SubstanceDto substance;
    private EmissionCompartmentDto emissionCompartment;
    private UnitProjectImpactDto unit;
}
