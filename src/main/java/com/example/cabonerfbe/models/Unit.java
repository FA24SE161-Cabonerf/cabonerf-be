package com.example.cabonerfbe.models;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Unit extends Base{
    private String name;
    private double conversionFactor;
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "unit_group_id")
    private UnitGroup unitGroup;

}
