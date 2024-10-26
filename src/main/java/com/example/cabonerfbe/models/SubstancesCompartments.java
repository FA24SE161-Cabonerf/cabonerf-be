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
public class SubstancesCompartments extends Base{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_substance_id", nullable = false)
    private EmissionSubstances emissionSubstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_compartment_id", nullable = false)
    private EmissionCompartment emissionCompartment;
}
