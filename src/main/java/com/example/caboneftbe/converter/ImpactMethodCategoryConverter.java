package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.ImpactCategoryDto;
import com.example.caboneftbe.dto.ImpactMethodCategoryDto;
import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.models.ImpactMethodCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImpactMethodCategoryConverter {
    ImpactMethodCategoryConverter INSTANCE = Mappers.getMapper(ImpactMethodCategoryConverter.class);

    ImpactMethodCategoryDto fromImpactMethodCategoryToImpactMethodCategoryDto(ImpactMethodCategory impactMethodCategory);
}
