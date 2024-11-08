package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ExchangesDto {
    private UUID id;
    private String name;
    private double value;
    private ExchangesTypeDto exchangesType;
    private EmissionSubstanceDto emissionSubstance;
    private UnitDto unit;
    private boolean input;

}
