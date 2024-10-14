package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.PerspectiveDto;
import com.example.caboneftbe.dto.UnitDto;
import com.example.caboneftbe.models.Perspective;
import com.example.caboneftbe.models.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UnitConverter {
    UnitConverter INSTANCE = Mappers.getMapper(UnitConverter.class);

    UnitDto fromUnitToUnitDto(Unit unit);
}
