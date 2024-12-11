package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Impact method category dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImpactMethodCategoryDto {
    private UUID id;
    private LifeCycleImpactAssessmentMethodDto lifeCycleImpactAssessmentMethod;
    private ProjectImpactCategoryDto impactCategory;
}
