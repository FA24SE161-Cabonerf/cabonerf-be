package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Exchanges.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Exchanges extends Base {

    private String name;
    @Column(length = 1000)
    private String description;
    @Column(precision = 100, scale = 60)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "exchanges_type_id")
    private ExchangesType exchangesType;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    private boolean input;

    @ManyToOne
    @JoinColumn(name = "emission_substance_id")
    private EmissionSubstance emissionSubstance;

    /**
     * Gets process id.
     *
     * @return the process id
     */
    public UUID getProcessId() {
        return process != null ? process.getId() : null;
    }
}
