package com.example.cabonerfbe.dto;

import com.example.cabonerfbe.models.EmissionSubstances;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SubstancesCompartmentsDto {
    private UUID id;
    private EmissionSubstancesDto emissionSubstance;
    private EmissionCompartmentDto emissionCompartment;
    private UnitProjectImpactDto unit;
}
