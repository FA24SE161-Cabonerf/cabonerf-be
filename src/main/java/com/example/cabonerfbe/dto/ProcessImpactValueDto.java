package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessImpactValueDto {
    private UUID id;
    private double unitLevel;
    private double systemLevel;
    private double overallImpactContribution;
    private MethodDto method;
    private ProjectImpactCategoryDto impactCategory;
    private UnitProjectImpactDto unit;
}
