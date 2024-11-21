package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.GetProjectByIdDto;
import com.example.cabonerfbe.dto.ProjectDto;
import com.example.cabonerfbe.dto.UpdateProjectDto;
import com.example.cabonerfbe.models.Project;
import com.example.cabonerfbe.response.ProjectCalculationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectConverter {
    ProjectConverter INSTANCE = Mappers.getMapper(ProjectConverter.class);

    ProjectDto fromProjectToProjectDto(Project project);

    @Mapping(source = "lifeCycleImpactAssessmentMethod", target = "method")
    @Mapping(source = "user", target = "owner")
    ProjectDto toDto(Project project);

    @Mapping(source = "lifeCycleImpactAssessmentMethod", target = "method")
    UpdateProjectDto fromDetailToDto(Project project);

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
        if (project.getProcesses().isEmpty()) {
            System.out.println("process list empty o converter");
        }
        projectCalculationResponse.setProcesses(project.getProcesses());
        if (project.getConnectors().isEmpty()) {
            System.out.println("connector list empty o converter");
        }
        projectCalculationResponse.setConnectors(project.getConnectors());

        return projectCalculationResponse;
    }

    ;
}
