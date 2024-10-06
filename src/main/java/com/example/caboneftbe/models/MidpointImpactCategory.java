package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MidpointImpactCategory extends Base{
    private String name;
    private String description;
    private String abbr;

    @ManyToOne
    @JoinColumn(name = "midpoint_impact_category_unit_id")
    private MidpointImpactCategoryUnit unit;

    @ManyToOne
    @JoinColumn(name = "impact_category_id")
    private ImpactCategory impactCategory;
}
