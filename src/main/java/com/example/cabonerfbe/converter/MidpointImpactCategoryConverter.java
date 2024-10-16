package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.MidpointImpactCategoryDto;
import com.example.cabonerfbe.models.MidpointImpactCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MidpointImpactCategoryConverter {
    MidpointImpactCategoryConverter INSTANCE = Mappers.getMapper(MidpointImpactCategoryConverter.class);

    MidpointImpactCategoryDto fromMidpointImpactCategoryToMidpointImpactCategoryDto(MidpointImpactCategory midpointImpactCategory);
}
