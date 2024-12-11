package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * The class Unit.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Unit extends Base {
    private String name;
    @Column(precision = 100, scale = 60)
    private BigDecimal conversionFactor;
    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "unit_group_id")
    private UnitGroup unitGroup;

}
