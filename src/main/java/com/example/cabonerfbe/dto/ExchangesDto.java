package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ExchangesDto {
    private long id;
    private String name;
    private double value;
    private ExchangesTypeDto exchangesType;
    private UnitDto unit;
    private boolean input;

}
