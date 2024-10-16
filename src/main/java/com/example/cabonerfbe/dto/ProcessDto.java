package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessDto {
    private long id;
    private String name;
    private String description;
    private LifeCycleStageDto lifeCycleStage;
    private String overallProductFlowRequired;
    private ProjectDto project;
}
