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
public class Process extends Base{

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "lifecycle_stage_id")
    private LifeCycleStage lifeCycleStage;
    @Column(precision = 100, scale = 60)
    private BigDecimal overAllProductFlowRequired;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
