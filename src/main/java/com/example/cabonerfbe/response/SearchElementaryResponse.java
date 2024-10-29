package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.dto.EmissionSubstancesDto;
import com.example.cabonerfbe.dto.FactorDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SearchElementaryResponse {
    UUID id;
    EmissionSubstancesDto emissionSubstances;
    EmissionCompartmentDto emissionCompartment;
    List<FactorDto> factors;
}
