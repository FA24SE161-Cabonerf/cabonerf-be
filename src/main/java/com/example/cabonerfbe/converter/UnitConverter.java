package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UnitDto;
import com.example.cabonerfbe.models.Unit;
import com.example.cabonerfbe.request.CreateUnitRequest;
import com.example.cabonerfbe.response.UnitResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitConverter {
    UnitConverter INSTANCE = Mappers.getMapper(UnitConverter.class);

    UnitDto fromUnitToUnitDto(Unit unit);


    List<UnitResponse> fromUnitListToUnitResponseList(List<Unit> units);

    default Unit fromUnitRequestToUnit(CreateUnitRequest request){
        Unit newUnit = new Unit();
        newUnit.setName(request.getUnitName());
        newUnit.setConversionFactor(request.getConversionFactor());
        newUnit.setIsDefault(request.getIsDefault());
        return newUnit;
    };

    UnitResponse fromUnitToUnitResponse(Unit savedUnit);
    List<UnitDto> fromListUnitToUnitDto(List<Unit> units);
}
