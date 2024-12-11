package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The class Get project by id dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class GetProjectByIdDto {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private boolean favorite;
    private SystemBoundaryDto systemBoundary;
    private MethodDto method;
    private IndustryCodeDto industryCode;
    private List<ProjectImpactDto> impacts;
    private List<ProcessDto> processes;
    private List<ConnectorDto> connectors;
    private List<LifeCycleBreakdownPercentDto> lifeCycleStageBreakdown;

}
