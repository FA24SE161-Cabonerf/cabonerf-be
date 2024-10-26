package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectImpactValueDto {
    private UUID id;
    private double value;
    private ImpactMethodCategoryDto impactMethodCategory;
}
