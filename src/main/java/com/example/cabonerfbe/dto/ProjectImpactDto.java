package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectImpactDto {
    private long id;
    private double value;
    private MethodDto method;
    private ProjectImpactCategoryDto impactCategory;
    private UnitProjectImpactDto unit;
}
