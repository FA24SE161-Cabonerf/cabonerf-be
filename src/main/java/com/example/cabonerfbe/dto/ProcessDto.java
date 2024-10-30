package com.example.cabonerfbe.dto;

import lombok.*;

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
    private double overallProductFlowRequired;
    private List<ProcessImpactValueDto> impacts;
    private List<ExchangesDto> exchanges;
}
