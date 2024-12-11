package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.IndustryCodeDto;
import com.example.cabonerfbe.models.IndustryCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Industry code converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface IndustryCodeConverter {
    /**
     * The constant INSTANCE.
     */
    IndustryCodeConverter INSTANCE = Mappers.getMapper(IndustryCodeConverter.class);

    /**
     * Model to dto method.
     *
     * @param industryCode the industry code
     * @return the industry code dto
     */
    IndustryCodeDto modelToDto(IndustryCode industryCode);
}
