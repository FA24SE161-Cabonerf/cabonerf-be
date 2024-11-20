package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.CarbonIntensityDto;
import com.example.cabonerfbe.models.CarbonIntensity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarbonIntensityConverter {
    CarbonIntensityConverter INSTANCE = Mappers.getMapper(CarbonIntensityConverter.class);

    CarbonIntensityDto toDto(CarbonIntensity carbonIntensity);
}
