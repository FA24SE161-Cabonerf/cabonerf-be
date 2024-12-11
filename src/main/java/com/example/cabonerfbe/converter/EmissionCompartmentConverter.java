package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.EmissionCompartmentDto;
import com.example.cabonerfbe.models.EmissionCompartment;
import com.example.cabonerfbe.response.EmissionCompartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The interface Emission compartment converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface EmissionCompartmentConverter {
    /**
     * The constant INSTANCE.
     */
    EmissionCompartmentConverter INSTANCE = Mappers.getMapper(EmissionCompartmentConverter.class);

    /**
     * From emission compartment to emission compartment dto method.
     *
     * @param emissionCompartment the emission compartment
     * @return the emission compartment dto
     */
    EmissionCompartmentDto fromEmissionCompartmentToEmissionCompartmentDto(EmissionCompartment emissionCompartment);

    /**
     * From list emission compartment to list emission compartment response method.
     *
     * @param emissionCompartmentList the emission compartment list
     * @return the list
     */
    List<EmissionCompartmentResponse> fromListEmissionCompartmentToListEmissionCompartmentResponse(List<EmissionCompartment> emissionCompartmentList);
}
