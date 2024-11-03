package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.models.EmissionCompartment;
import com.example.cabonerfbe.response.EmissionCompartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmissionCompartmentConverter {
    EmissionCompartmentConverter INSTANCE = Mappers.getMapper(EmissionCompartmentConverter.class);

    EmissionCompartmentDto fromEmissionCompartmentToEmissionCompartmentDto(EmissionCompartment emissionCompartment);

    List<EmissionCompartmentResponse> fromListEmissionCompartmentToListEmissionCompartmentResponse(List<EmissionCompartment> emissionCompartmentList);
}
