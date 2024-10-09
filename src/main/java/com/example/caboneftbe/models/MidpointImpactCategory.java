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
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "impact_category_id")
    private ImpactCategory impactCategory;
}
