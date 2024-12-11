package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Process.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class Process extends Base {

    private String name;
    private String description;
    @Column(nullable = true)
    private UUID methodId;
    private boolean library;

    @ManyToOne
    @JoinColumn(name = "system_boundary_id", nullable = true)
    private SystemBoundary systemBoundary;

    @ManyToOne
    @JoinColumn(name = "lifecycle_stage_id")
    private LifeCycleStage lifeCycleStage;
    @Column(precision = 100, scale = 60)
    private BigDecimal overAllProductFlowRequired;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;

}
