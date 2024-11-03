package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessDetailDto {
    private UUID id;
    private String name;
    private String description;
    private LifeCycleStageDto lifeCycleStages;
}
