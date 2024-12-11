package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * The class Midpoint impact characterization factors.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MidpointImpactCharacterizationFactors extends Base {
    @ManyToOne
    @JoinColumn(name = "impact_method_category_id")
    private ImpactMethodCategory impactMethodCategory;

    @ManyToOne
    @JoinColumn(name = "emission_substance_id")
    private EmissionSubstance emissionSubstance;

    private String scientificValue;
    @Column(precision = 100, scale = 60)
    private BigDecimal decimalValue;

}
