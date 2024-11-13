package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ProjectImpactValue extends Base{

    @ManyToOne
    @JoinColumn(name = "impact_method_category_id")
    private ImpactMethodCategory impactMethodCategory;
    @Column(precision = 100, scale = 60)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
