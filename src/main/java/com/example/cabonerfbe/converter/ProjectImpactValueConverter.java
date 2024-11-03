package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ProjectImpactValueDto;
import com.example.cabonerfbe.models.ProjectImpactValue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectImpactValueConverter {
    ProjectImpactValueConverter INSTANCE = Mappers.getMapper(ProjectImpactValueConverter.class);

    ProjectImpactValueDto fromProjectImpactValueToProjectImpactValueDto(ProjectImpactValue projectImpactValue);
    List<ProjectImpactValueDto> fromListProjectImpactValueToProjectImpactValueDto(List<ProjectImpactValue> list);
}
