package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.EmissionSubstancesDto;
import com.example.caboneftbe.dto.ImpactCategoryDto;
import com.example.caboneftbe.dto.ImpactMethodCategoryDto;
import com.example.caboneftbe.models.EmissionSubstances;
import com.example.caboneftbe.models.ImpactCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImpactCategoryConverter {
    ImpactCategoryConverter INSTANCE = Mappers.getMapper(ImpactCategoryConverter.class);

    ImpactCategoryDto fromImpactCategoryToImpactCategoryDto(ImpactCategory emissionSubstances);
}
