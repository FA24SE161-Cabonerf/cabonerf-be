package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.PerspectiveDto;
import com.example.cabonerfbe.models.Perspective;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PerspectiveConverter {
    PerspectiveConverter INSTANCE = Mappers.getMapper(PerspectiveConverter.class);

    PerspectiveDto fromPerspectiveToPerspectiveDto(Perspective perspective);
}
