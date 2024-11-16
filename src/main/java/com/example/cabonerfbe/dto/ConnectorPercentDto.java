package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ConnectorPercentDto {
    private UUID id;
    private UUID startProcessId;
    private UUID endProcessId;
    private UUID startExchangesId;
    private UUID endExchangesId;
    private BigDecimal percent;
}