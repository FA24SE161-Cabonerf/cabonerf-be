package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ImpactMethodCategory extends Base {

    @ManyToOne
    @JoinColumn(name = "life_cycle_impact_assessment_method_id")
    private LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod;

    @ManyToOne
    @JoinColumn(name = "impact_category_id")
    private ImpactCategory impactCategory;
}
