package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.MidpointImpactCategoryDto;
import com.example.cabonerfbe.dto.ProjectMidpointDto;
import com.example.cabonerfbe.models.MidpointImpactCategory;
import com.example.cabonerfbe.response.MidpointImpactCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Midpoint impact category converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface MidpointImpactCategoryConverter {
    /**
     * The constant INSTANCE.
     */
    MidpointImpactCategoryConverter INSTANCE = Mappers.getMapper(MidpointImpactCategoryConverter.class);

    /**
     * From midpoint impact category to midpoint impact category dto method.
     *
     * @param midpointImpactCategory the midpoint impact category
     * @return the midpoint impact category dto
     */
    MidpointImpactCategoryDto fromMidpointImpactCategoryToMidpointImpactCategoryDto(MidpointImpactCategory midpointImpactCategory);

    /**
     * From m project to midpoint impact category dto method.
     *
     * @param midpointImpactCategory the midpoint impact category
     * @return the project midpoint dto
     */
    ProjectMidpointDto fromMProjectToMidpointImpactCategoryDto(MidpointImpactCategory midpointImpactCategory);

    /**
     * From list midpoint impact category to list midpoint impact category response method.
     *
     * @param midpointImpactCategoryList the midpoint impact category list
     * @return the list
     */
    List<MidpointImpactCategoryResponse> fromListMidpointImpactCategoryToListMidpointImpactCategoryResponse(List<MidpointImpactCategory> midpointImpactCategoryList);
}
