package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Emission substance dto.
 *
 * @author SonPHH.
 */
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
