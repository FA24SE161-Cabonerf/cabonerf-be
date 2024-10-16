package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ImpactMethodCategoryDto;
import com.example.cabonerfbe.models.ImpactMethodCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImpactMethodCategoryConverter {
    ImpactMethodCategoryConverter INSTANCE = Mappers.getMapper(ImpactMethodCategoryConverter.class);

    ImpactMethodCategoryDto fromImpactMethodCategoryToImpactMethodCategoryDto(ImpactMethodCategory impactMethodCategory);
}
