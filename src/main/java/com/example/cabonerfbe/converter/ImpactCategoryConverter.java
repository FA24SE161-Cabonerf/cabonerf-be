package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ImpactCategoryDto;
import com.example.cabonerfbe.dto.LifeCycleBreakdownPercentDto;
import com.example.cabonerfbe.dto.ProjectImpactCategoryDto;
import com.example.cabonerfbe.models.ImpactCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Impact category converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ImpactCategoryConverter {
    /**
     * The constant INSTANCE.
     */
    ImpactCategoryConverter INSTANCE = Mappers.getMapper(ImpactCategoryConverter.class);

    /**
     * From impact category list to dto list method.
     *
     * @param impactCategories the impact categories
     * @return the list
     */
    List<ImpactCategoryDto> fromImpactCategoryListToDtoList(List<ImpactCategory> impactCategories);

    /**
     * From impact category to impact category dto method.
     *
     * @param emissionSubstances the emission substances
     * @return the impact category dto
     */
    ImpactCategoryDto fromImpactCategoryToImpactCategoryDto(ImpactCategory emissionSubstances);

    /**
     * From project to impact category dto method.
     *
     * @param impactCategory the impact category
     * @return the project impact category dto
     */
    ProjectImpactCategoryDto fromProjectToImpactCategoryDto(ImpactCategory impactCategory);

    /**
     * From category to break down method.
     *
     * @param impactCategory the impact category
     * @return the life cycle breakdown percent dto
     */
    LifeCycleBreakdownPercentDto fromCategoryToBreakDown(ImpactCategory impactCategory);
}
