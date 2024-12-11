package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UnitGroupDto;
import com.example.cabonerfbe.models.UnitGroup;
import com.example.cabonerfbe.response.UnitGroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Unit group converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface UnitGroupConverter {
    /**
     * The constant INSTANCE.
     */
    UnitGroupConverter INSTANCE = Mappers.getMapper(UnitGroupConverter.class);

    /**
     * From unit group to unit group dto method.
     *
     * @param unitGroup the unit group
     * @return the unit group dto
     */
    UnitGroupDto fromUnitGroupToUnitGroupDto(UnitGroup unitGroup);

    /**
     * From list unit group to unit group dto method.
     *
     * @param unitGroups the unit groups
     * @return the list
     */
    List<UnitGroupDto> fromListUnitGroupToUnitGroupDto(List<UnitGroup> unitGroups);

    /**
     * From unit group to unit group response method.
     *
     * @param unitGroup the unit group
     * @return the unit group response
     */
    @Mapping(source = "name", target = "unitGroupName")
    UnitGroupResponse fromUnitGroupToUnitGroupResponse(UnitGroup unitGroup);
}
