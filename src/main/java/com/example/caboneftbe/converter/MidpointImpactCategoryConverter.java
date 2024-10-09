package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.MidpointImpactCategoryDto;
import com.example.caboneftbe.models.MidpointImpactCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MidpointImpactCategoryConverter {
    MidpointImpactCategoryConverter INSTANCE = Mappers.getMapper(MidpointImpactCategoryConverter.class);

    MidpointImpactCategoryDto fromMidpointImpactCategoryToMidpointImpactCategoryDto(MidpointImpactCategory midpointImpactCategory);
}
