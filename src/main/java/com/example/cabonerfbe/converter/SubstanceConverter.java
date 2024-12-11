package com.example.cabonerfbe.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Substance converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface SubstanceConverter {
    /**
     * The constant INSTANCE.
     */
    SubstanceConverter INSTANCE = Mappers.getMapper(SubstanceConverter.class);

}
