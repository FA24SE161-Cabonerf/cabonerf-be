package com.example.cabonerfbe.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class CarbonIntensity extends Base {
    private String category;
    private BigDecimal value;
    private String unit;
    private String description;
    @Column(length = 90000)
    private String icon;
    @Column(length = 90000)
    private String ref;
    @Column(length = 90000)
    private String ref_description;
}
