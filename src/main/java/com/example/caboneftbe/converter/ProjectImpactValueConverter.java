package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.ProjectDto;
import com.example.caboneftbe.dto.ProjectImpactValueDto;
import com.example.caboneftbe.models.Project;
import com.example.caboneftbe.models.ProjectImpactValue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectImpactValueConverter {
    ProjectImpactValueConverter INSTANCE = Mappers.getMapper(ProjectImpactValueConverter.class);

    ProjectImpactValueDto fromProjectImpactValueToProjectImpactValueDto(ProjectImpactValue projectImpactValue);
    List<ProjectImpactValueDto> fromListProjectImpactValueToProjectImpactValueDto(List<ProjectImpactValue> list);
}
