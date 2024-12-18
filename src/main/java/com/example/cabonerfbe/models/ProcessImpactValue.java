package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Process impact value.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ProcessImpactValue extends Base {
    @ManyToOne
    @JoinColumn(name = "impact_method_category_id")
    private ImpactMethodCategory impactMethodCategory;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;
    @Column(precision = 100, scale = 60)
    private BigDecimal unitLevel;
    @Column(precision = 100, scale = 60)
    private BigDecimal systemLevel;
    @Column(precision = 100, scale = 60)
    private BigDecimal overallImpactContribution;
    @Column(precision = 100, scale = 60)
    private BigDecimal previousProcessValue;

    @Version
    @Column
    private int version = 1;

    /**
     * Gets process id.
     *
     * @return the process id
     */
    public UUID getProcessId() {
        return process != null ? process.getId() : null;
    }

    /**
     * Gets impact method category id.
     *
     * @return the impact method category id
     */
    public UUID getImpactMethodCategoryId() {
        return impactMethodCategory.getId();
    }
}
