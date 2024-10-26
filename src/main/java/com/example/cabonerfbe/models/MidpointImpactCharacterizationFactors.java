package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MidpointImpactCharacterizationFactors extends Base{
    private String cas;
    @ManyToOne
    @JoinColumn(name = "impact_method_category_id")
    private ImpactMethodCategory impactMethodCategory;

    @ManyToOne
    @JoinColumn(name = "substances_compartments_id")
    private SubstancesCompartments substancesCompartments;

    private String scientificValue;
    private double decimalValue;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

}
