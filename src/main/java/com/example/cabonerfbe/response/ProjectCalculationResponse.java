package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectCalculationResponse {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private MethodDto method;
    private List<ProjectImpactDto> impacts;
    private List<ProcessDto> processes;
    private List<ConnectorDto> connectors;
    ProcessNodeDto contributionBreakdown;
}
