package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectImpactValueDto {
    private long id;
    private double value;
    private ImpactMethodCategoryDto impactMethodCategory;
}
