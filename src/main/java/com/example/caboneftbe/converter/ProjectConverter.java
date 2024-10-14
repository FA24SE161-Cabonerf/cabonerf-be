package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.ExchangesDto;
import com.example.caboneftbe.dto.ProjectDto;
import com.example.caboneftbe.models.Exchanges;
import com.example.caboneftbe.models.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectConverter {
    ProjectConverter INSTANCE = Mappers.getMapper(ProjectConverter.class);

    ProjectDto fromProjectToProjectDto(Project project);
}
