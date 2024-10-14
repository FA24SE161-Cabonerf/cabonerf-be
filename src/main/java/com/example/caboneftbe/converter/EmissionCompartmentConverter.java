package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.EmissionCompartmentDto;
import com.example.caboneftbe.dto.RoleDto;
import com.example.caboneftbe.models.EmissionCompartment;
import com.example.caboneftbe.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface EmissionCompartmentConverter {
    EmissionCompartmentConverter INSTANCE = Mappers.getMapper(EmissionCompartmentConverter.class);

    EmissionCompartmentDto fromEmissionCompartmentToEmissionCompartmentDto(EmissionCompartment emissionCompartment);
}
