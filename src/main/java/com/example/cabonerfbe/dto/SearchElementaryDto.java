package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SearchElementaryDto {
    SubstancesCompartmentsDto substancesCompartments;
    List<FactorDto> factors;
}
