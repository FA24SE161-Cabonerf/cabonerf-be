package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ProjectDto;
import com.example.cabonerfbe.models.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectConverter {
    ProjectConverter INSTANCE = Mappers.getMapper(ProjectConverter.class);

    ProjectDto fromProjectToProjectDto(Project project);
}