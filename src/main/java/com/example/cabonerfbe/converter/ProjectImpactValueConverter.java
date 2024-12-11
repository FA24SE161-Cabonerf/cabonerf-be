package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ProjectImpactValueDto;
import com.example.cabonerfbe.models.ProjectImpactValue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Project impact value converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ProjectImpactValueConverter {
    /**
     * The constant INSTANCE.
     */
    ProjectImpactValueConverter INSTANCE = Mappers.getMapper(ProjectImpactValueConverter.class);

    /**
     * From project impact value to project impact value dto method.
     *
     * @param projectImpactValue the project impact value
     * @return the project impact value dto
     */
    ProjectImpactValueDto fromProjectImpactValueToProjectImpactValueDto(ProjectImpactValue projectImpactValue);

    /**
     * From list project impact value to project impact value dto method.
     *
     * @param list the list
     * @return the list
     */
    List<ProjectImpactValueDto> fromListProjectImpactValueToProjectImpactValueDto(List<ProjectImpactValue> list);
}
