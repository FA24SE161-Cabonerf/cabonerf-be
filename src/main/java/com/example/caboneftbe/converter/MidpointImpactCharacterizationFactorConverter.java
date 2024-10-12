package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.MidpointImpactCategoryDto;
import com.example.caboneftbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.caboneftbe.models.MidpointImpactCategory;
import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MidpointImpactCharacterizationFactorConverter {
    MidpointImpactCharacterizationFactorConverter INSTANCE = Mappers.getMapper(MidpointImpactCharacterizationFactorConverter.class);

    MidpointImpactCharacterizationFactorsDto fromMidpointImpactCharacterizationFactorsToMidpointImpactCharacterizationFactorsDto(MidpointImpactCharacterizationFactors midpointImpactCharacterizationFactors);
}
