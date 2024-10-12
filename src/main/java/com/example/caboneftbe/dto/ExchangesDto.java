package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.ExchangesType;
import com.example.caboneftbe.models.Process;
import com.example.caboneftbe.models.Unit;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ExchangesDto {
    private long id;
    private String name;
    private String description;
    private double value;
    private ExchangesTypeDto exchangesType;
    private UnitDto unit;
    private boolean input;
}
