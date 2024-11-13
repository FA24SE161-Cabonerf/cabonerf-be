package com.example.cabonerfbe.models;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Unit extends Base{
    private String name;
    @Column(precision = 100, scale = 60)
    private BigDecimal conversionFactor;
    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "unit_group_id")
    private UnitGroup unitGroup;

}
