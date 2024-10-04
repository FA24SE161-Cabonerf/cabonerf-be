package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ProcessImpactValue extends Base{
    @ManyToOne
    @JoinColumn(name = "impact_method_category_id")
    private ImpactMethodCategory impactMethodCategory;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    private double unitLevel;
    private double systemLevel;
    private double overallImpactContribution;
    private double previousProcessValue;
}
