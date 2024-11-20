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

    @Mapping(target = "contributionBreakdown", ignore = true)
    ProjectCalculationResponse fromGetProjectDtoToCalculateResponse(GetProjectByIdDto project);
}
