package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ImpactCategory extends Base{
    private String name;
    private String indicator;
    private String unit;

    @ManyToOne
    @JoinColumn(name = "midpoint_impact_category_id")
    private MidpointImpactCategory midpointImpactCategory;
}
