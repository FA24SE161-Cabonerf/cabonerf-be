package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.ImpactCategoryDto;
import com.example.caboneftbe.models.ImpactCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImpactCategoryConverter {
    ImpactCategoryConverter INSTANCE = Mappers.getMapper(ImpactCategoryConverter.class);

    List<ImpactCategoryDto> fromImpactCategoryListToDtoList(List<ImpactCategory> impactCategories);
}
