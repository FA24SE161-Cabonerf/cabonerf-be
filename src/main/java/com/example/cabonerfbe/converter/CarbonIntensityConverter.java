package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.CarbonIntensityDto;
import com.example.cabonerfbe.models.CarbonIntensity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Carbon intensity converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface CarbonIntensityConverter {
    /**
     * The constant INSTANCE.
     */
    CarbonIntensityConverter INSTANCE = Mappers.getMapper(CarbonIntensityConverter.class);

    /**
     * To dto method.
     *
     * @param carbonIntensity the carbon intensity
     * @return the carbon intensity dto
     */
    CarbonIntensityDto toDto(CarbonIntensity carbonIntensity);
}
