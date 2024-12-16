package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.GetProjectByIdDto;
import com.example.cabonerfbe.dto.ProjectDto;
import com.example.cabonerfbe.response.ProjectWithProcessResponse;
import com.example.cabonerfbe.dto.UpdateProjectDto;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.response.ProjectCalculationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface Project converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ProjectConverter {
    /**
     * The constant INSTANCE.
     */
    ProjectConverter INSTANCE = Mappers.getMapper(ProjectConverter.class);

    /**
     * From project to project dto method.
     *
     * @param project the project
     * @return the project dto
     */
    ProjectDto fromProjectToProjectDto(Project project);

    /**
     * To dto method.
     *
     * @param project the project
     * @return the project dto
     */
    @Mapping(source = "lifeCycleImpactAssessmentMethod", target = "method")
    @Mapping(source = "user", target = "owner")
    ProjectDto toDto(Project project);

    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "lifeCycleStageBreakdown", ignore = true)
    @Mapping(target = "intensity", ignore = true)
    @Mapping(target = "impacts", ignore = true)
    @Mapping(target = "functionalUnit", ignore = true)
    @Mapping(source = "lifeCycleImpactAssessmentMethod", target = "method")
    @Mapping(source = "user", target = "owner")
    ProjectWithProcessResponse toProjectWithProcessDto(Project project);

    /**
     * From detail to dto method.
     *
     * @param project the project
     * @return the update project dto
     */
    @Mapping(source = "lifeCycleImpactAssessmentMethod", target = "method")
    UpdateProjectDto fromDetailToDto(Project project);

    /**
     * From get project dto to calculate response method.
     *
     * @param project the project
     * @return the project calculation response
     */
    default ProjectCalculationResponse fromGetProjectDtoToCalculateResponse(GetProjectByIdDto project) {
        if (project == null) {
            return null;
        }
        ProjectCalculationResponse projectCalculationResponse = new ProjectCalculationResponse();

        projectCalculationResponse.setId(project.getId());
        projectCalculationResponse.setName(project.getName());
        projectCalculationResponse.setDescription(project.getDescription());
        projectCalculationResponse.setLocation(project.getLocation());
        projectCalculationResponse.setMethod(project.getMethod());
        projectCalculationResponse.setImpacts(project.getImpacts());
        projectCalculationResponse.setProcesses(project.getProcesses());
        projectCalculationResponse.setConnectors(project.getConnectors());

        return projectCalculationResponse;
    }

}
