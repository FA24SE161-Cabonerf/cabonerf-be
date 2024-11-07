package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SearchSubstancesCompartmentsDto {
    private UUID id;
    private EmissionSubstancesDto emissionSubstance;
    private EmissionCompartmentDto emissionCompartment;
    List<FactorDto> factors;
}
