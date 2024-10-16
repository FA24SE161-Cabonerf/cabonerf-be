package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Exchanges extends Base{

    private String name;
    private String description;
    private double value;

    @ManyToOne
    @JoinColumn(name = "exchanges_type_id")
    private ExchangesType exchangesType;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    private boolean input;
}
