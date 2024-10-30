package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class FactorDto {
    UUID id;
    String cas;
    ProjectImpactCategoryDto impactCategory;
    String scientificValue;
    double decimalValue;
}
