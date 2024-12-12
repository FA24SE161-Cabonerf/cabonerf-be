package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The class Project calculation response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectCalculationResponse {
    /**
     * The Contribution breakdown.
     */
    ProcessNodeDto contributionBreakdown;
    private UUID id;
    private String name;
    private String description;
    private String location;
    private boolean favorite;
    private SystemBoundaryDto systemBoundary;
    private MethodDto method;
    private List<ProjectImpactDto> impacts;
    private List<ProcessDto> processes;
    private List<ConnectorDto> connectors;
    private List<LifeCycleBreakdownPercentDto> lifeCycleStageBreakdown;
}
