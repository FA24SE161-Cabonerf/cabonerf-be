package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.MidpointImpactCharacterizationFactorsDto;
import com.example.caboneftbe.dto.PerspectiveDto;
import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;
import com.example.caboneftbe.models.Perspective;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PerspectiveConverter {
    PerspectiveConverter INSTANCE = Mappers.getMapper(PerspectiveConverter.class);

    PerspectiveDto fromPerspectiveToPerspectiveDto(Perspective perspective);
}
