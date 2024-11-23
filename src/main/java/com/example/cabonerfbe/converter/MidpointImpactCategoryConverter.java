package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.MidpointImpactCategoryDto;
import com.example.cabonerfbe.dto.ProjectMidpointDto;
import com.example.cabonerfbe.models.MidpointImpactCategory;
import com.example.cabonerfbe.response.MidpointImpactCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MidpointImpactCategoryConverter {
    MidpointImpactCategoryConverter INSTANCE = Mappers.getMapper(MidpointImpactCategoryConverter.class);

    MidpointImpactCategoryDto fromMidpointImpactCategoryToMidpointImpactCategoryDto(MidpointImpactCategory midpointImpactCategory);

    ProjectMidpointDto fromMProjectToMidpointImpactCategoryDto(MidpointImpactCategory midpointImpactCategory);

    List<MidpointImpactCategoryResponse> fromListMidpointImpactCategoryToListMidpointImpactCategoryResponse(List<MidpointImpactCategory> midpointImpactCategoryList);
}
