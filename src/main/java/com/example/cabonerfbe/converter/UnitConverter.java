package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UnitDto;
import com.example.cabonerfbe.dto.UnitProjectImpactDto;
import com.example.cabonerfbe.models.Unit;
import com.example.cabonerfbe.request.CreateUnitRequest;
import com.example.cabonerfbe.response.UnitResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Unit converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface UnitConverter {
    /**
     * The constant INSTANCE.
     */
    UnitConverter INSTANCE = Mappers.getMapper(UnitConverter.class);

    /**
     * From unit to unit dto method.
     *
     * @param unit the unit
     * @return the unit dto
     */
    UnitDto fromUnitToUnitDto(Unit unit);


    /**
     * From unit list to unit response list method.
     *
     * @param units the units
     * @return the list
     */
    List<UnitResponse> fromUnitListToUnitResponseList(List<Unit> units);

    /**
     * From unit request to unit method.
     *
     * @param request the request
     * @return the unit
     */
    default Unit fromUnitRequestToUnit(CreateUnitRequest request) {
        Unit newUnit = new Unit();
        newUnit.setName(request.getUnitName());
        newUnit.setConversionFactor(request.getConversionFactor());
        newUnit.setIsDefault(request.getIsDefault());
        return newUnit;
    }

    /**
     * From unit to unit response method.
     *
     * @param savedUnit the saved unit
     * @return the unit response
     */
    UnitResponse fromUnitToUnitResponse(Unit savedUnit);

    /**
     * From list unit to unit dto method.
     *
     * @param units the units
     * @return the list
     */
    List<UnitDto> fromListUnitToUnitDto(List<Unit> units);

    /**
     * From project to unit response method.
     *
     * @param unit the unit
     * @return the unit project impact dto
     */
    UnitProjectImpactDto fromProjectToUnitResponse(Unit unit);
}
