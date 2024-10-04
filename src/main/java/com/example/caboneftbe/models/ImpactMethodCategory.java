package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ImpactMethodCategory extends Base{

    private String name;

    @ManyToOne
    @JoinColumn(name = "life_cycle_impact_assessment_method_id")
    private LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod;

    @ManyToOne
    @JoinColumn(name = "impact_category_id")
    private ImpactCategory impactCategory;
}
