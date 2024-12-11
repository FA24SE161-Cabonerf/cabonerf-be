package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class Impact category.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ImpactCategory extends Base {
    private String name;
    private String indicator;
    private String indicatorDescription;
    private String unit;
    @Column(length = 9000)
    private String iconUrl;

    @ManyToOne
    @JoinColumn(name = "midpoint_impact_category_id")
    private MidpointImpactCategory midpointImpactCategory;

    @ManyToOne
    @JoinColumn(name = "emission_compartment_id")
    private EmissionCompartment emissionCompartment;
}
