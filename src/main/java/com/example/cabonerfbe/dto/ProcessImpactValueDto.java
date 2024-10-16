package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessImpactValueDto {
    private long id;
    private ImpactMethodCategoryDto impactMethodCategory;
    private double unitLevel;
    private double systemLevel;
    private double overallImpactContribution;
    private double previousProcessValue;
}
