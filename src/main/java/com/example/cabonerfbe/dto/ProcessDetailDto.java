package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Process detail dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessDetailDto {
    private UUID id;
    private String name;
    private String description;
    private LifeCycleStageDto lifeCycleStage;
}
