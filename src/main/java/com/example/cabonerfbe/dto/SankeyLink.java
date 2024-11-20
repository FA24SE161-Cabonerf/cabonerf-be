package com.example.cabonerfbe.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class SankeyLink {
    private UUID source;
    private UUID target;
    private BigDecimal value;

}
