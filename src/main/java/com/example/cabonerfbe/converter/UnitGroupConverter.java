package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UnitGroupDto;
import com.example.cabonerfbe.models.UnitGroup;
import com.example.cabonerfbe.response.UnitGroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitGroupConverter {
    UnitGroupConverter INSTANCE = Mappers.getMapper(UnitGroupConverter.class);

    UnitGroupDto fromUnitGroupToUnitGroupDto(UnitGroup unitGroup);
    List<UnitGroupDto> fromListUnitGroupToUnitGroupDto(List<UnitGroup> unitGroups);

    @Mapping(source = "name", target = "unitGroupName")
    UnitGroupResponse fromUnitGroupToUnitGroupResponse(UnitGroup unitGroup);
}
