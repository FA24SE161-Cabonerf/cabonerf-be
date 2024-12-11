package com.example.cabonerfbe.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * The class Project dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectDto {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private boolean favorite;
    private IndustryCodeDto industryCode;
    private MethodDto method;
    private LocalDate modifiedAt;
    private OwnerDto owner;
    private List<ProjectImpactDto> impacts;
    private List<LifeCycleBreakdownPercentDto> lifeCycleStageBreakdown;
    private List<CarbonIntensityDto> intensity;
    private String functionalUnit;
}
