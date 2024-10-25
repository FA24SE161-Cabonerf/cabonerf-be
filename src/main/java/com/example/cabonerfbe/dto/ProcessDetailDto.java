package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessDetailDto {
    private long id;
    private String name;
    private String description;
    private LifeCycleStageDto lifeCycleStage;
}
