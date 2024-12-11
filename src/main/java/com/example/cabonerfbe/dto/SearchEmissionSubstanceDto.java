package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The class Search emission substance dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SearchEmissionSubstanceDto {
    /**
     * The Factors.
     */
    List<FactorDto> factors;
    private UUID id;
    private SubstanceDto substance;
    private EmissionCompartmentDto emissionCompartment;
}
