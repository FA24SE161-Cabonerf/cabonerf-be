package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    private String description;
    private String abbr;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
