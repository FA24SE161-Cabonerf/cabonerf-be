package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

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

    private BigDecimal unitLevel;
    private BigDecimal systemLevel;
    private BigDecimal overallImpactContribution;
    private BigDecimal previousProcessValue;

    public UUID getProcessId() {
        return process != null ? process.getId() : null;
    }
}
