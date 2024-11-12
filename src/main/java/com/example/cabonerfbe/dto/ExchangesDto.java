package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ExchangesDto {
    private UUID id;
    private String name;
    private BigDecimal value;
    private ExchangesTypeDto exchangesType;
    private EmissionSubstanceDto emissionSubstance;
    private UnitDto unit;
    private boolean input;

}
