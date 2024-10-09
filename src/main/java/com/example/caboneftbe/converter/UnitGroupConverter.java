package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.UnitDto;
import com.example.caboneftbe.dto.UnitGroupDto;
import com.example.caboneftbe.models.Unit;
import com.example.caboneftbe.models.UnitGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UnitGroupConverter {
    UnitGroupConverter INSTANCE = Mappers.getMapper(UnitGroupConverter.class);

    UnitGroupDto fromUnitGroupToUnitGroupDto(UnitGroup unitGroup);
}
