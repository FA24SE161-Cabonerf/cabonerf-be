package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.ImpactMethodCategoryDto;
import com.example.cabonerfbe.models.ImpactMethodCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Impact method category converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface ImpactMethodCategoryConverter {
    /**
     * The constant INSTANCE.
     */
    ImpactMethodCategoryConverter INSTANCE = Mappers.getMapper(ImpactMethodCategoryConverter.class);

    /**
     * From impact method category to impact method category dto method.
     *
     * @param impactMethodCategory the impact method category
     * @return the impact method category dto
     */
    ImpactMethodCategoryDto fromImpactMethodCategoryToImpactMethodCategoryDto(ImpactMethodCategory impactMethodCategory);
}
