package com.example.cabonerfbe.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubstanceConverter {
    SubstanceConverter INSTANCE = Mappers.getMapper(SubstanceConverter.class);

}
