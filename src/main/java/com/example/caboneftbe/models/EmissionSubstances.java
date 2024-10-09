package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class EmissionSubstances extends Base{
    private String name;
    private String chemicalName;
    private String molecularFormula;
    private String alternativeFormula;

    @ManyToOne
    @JoinColumn(name = "emission_compartment_id")
    private EmissionCompartment emissionCompartment;
}
