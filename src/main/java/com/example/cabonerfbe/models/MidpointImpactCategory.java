package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class Midpoint impact category.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MidpointImpactCategory extends Base {
    private String name;
    @Column(length = 1000)
    private String description;
    private String abbr;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
