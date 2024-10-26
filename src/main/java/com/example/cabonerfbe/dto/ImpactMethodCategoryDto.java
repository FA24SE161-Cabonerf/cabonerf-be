package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImpactMethodCategoryDto {
    private UUID id;
    private LifeCycleImpactAssessmentMethodDto lifeCycleImpactAssessmentMethod;
    private ImpactCategoryDto impactCategory;
}
