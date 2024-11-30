package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ObjectLibraryDto {
    private UUID id;
    private String name;
    private String description;
    private LifeCycleStageDto lifeCycleStage;
    private boolean library;
    private List<ProcessImpactValueDto> impacts;
    private List<ExchangesDto> exchanges;
}
