package com.example.cabonerfbe.models;

import jakarta.persistence.*;
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
public class EmissionSubstance extends Base {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substance_id", nullable = false)
    private Substance substance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_compartment_id", nullable = true)
    private EmissionCompartment emissionCompartment;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    private Boolean isInput;
}
