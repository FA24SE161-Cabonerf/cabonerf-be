package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.models.EmissionCompartment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface EmissionCompartmentConverter {
    EmissionCompartmentConverter INSTANCE = Mappers.getMapper(EmissionCompartmentConverter.class);

    EmissionCompartmentDto fromEmissionCompartmentToEmissionCompartmentDto(EmissionCompartment emissionCompartment);
}
