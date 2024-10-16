package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImpactMethodCategoryDto {
    private long id;
    private LifeCycleImpactAssessmentMethodDto lifeCycleImpactAssessmentMethod;
    private ImpactCategoryDto impactCategory;
}
