package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SearchEmissionSubstanceDto {
    List<FactorDto> factors;
    private UUID id;
    private SubstanceDto substance;
    private EmissionCompartmentDto emissionCompartment;
}
