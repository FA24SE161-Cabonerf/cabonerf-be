package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class LifeCycleBreakdownPercentDto {
    private UUID id;
    private String name;
    private List<LifeCycleStagePercentDto> lifeCycleStage;
}
