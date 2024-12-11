package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * The class Project impact value.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ProjectImpactValue extends Base {

    @ManyToOne
    @JoinColumn(name = "impact_method_category_id")
    private ImpactMethodCategory impactMethodCategory;
    @Column(precision = 100, scale = 60)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
