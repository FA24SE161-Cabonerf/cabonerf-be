package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessImpactValueDto {
    private long id;
    private double unitLevel;
    private double systemLevel;
    private double overallImpactContribution;
    private MethodDto method;
    private ProjectImpactCategoryDto impactCategory;
    private UnitProjectImpactDto unit;
}
