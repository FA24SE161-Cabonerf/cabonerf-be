package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UnitDto;
import com.example.cabonerfbe.models.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitConverter {
    UnitConverter INSTANCE = Mappers.getMapper(UnitConverter.class);

    UnitDto fromUnitToUnitDto(Unit unit);
    List<UnitDto> fromListUnitToUnitDto(List<Unit> units);
}
