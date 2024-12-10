package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessDto {
    private UUID id;
    private String name;
    private String description;
    private LifeCycleStageDto lifeCycleStage;
    private UUID projectId;
    private boolean library;
    private BigDecimal overallProductFlowRequired;
    private List<ProcessImpactValueDto> impacts;
    private List<ExchangesDto> exchanges;
}
