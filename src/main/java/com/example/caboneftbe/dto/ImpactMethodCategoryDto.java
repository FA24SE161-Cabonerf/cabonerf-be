package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.LifeCycleImpactAssessmentMethod;
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
