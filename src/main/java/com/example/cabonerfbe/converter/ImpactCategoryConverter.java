package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.ProjectImpactCategoryDto;
import com.example.cabonerfbe.models.ImpactCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImpactCategoryConverter {
    ImpactCategoryConverter INSTANCE = Mappers.getMapper(ImpactCategoryConverter.class);

    List<ImpactCategoryDto> fromImpactCategoryListToDtoList(List<ImpactCategory> impactCategories);
    ImpactCategoryDto fromImpactCategoryToImpactCategoryDto(ImpactCategory emissionSubstances);
    ProjectImpactCategoryDto fromProjectToImpactCategoryDto(ImpactCategory impactCategory);
}
