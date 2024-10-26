package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectImpactDto {
    private UUID id;
    private double value;
    private MethodDto method;
    private ProjectImpactCategoryDto impactCategory;
    private UnitProjectImpactDto unit;
}
