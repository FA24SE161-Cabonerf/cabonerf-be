package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.EmissionCompartmentDto;
import com.example.caboneftbe.dto.EmissionSubstancesDto;
import com.example.caboneftbe.models.EmissionCompartment;
import com.example.caboneftbe.models.EmissionSubstances;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmissionSubstanceConverter {
    EmissionSubstanceConverter INSTANCE = Mappers.getMapper(EmissionSubstanceConverter.class);

    EmissionSubstancesDto fromEmissionSubstancesToEmissionSubstancesDto(EmissionSubstances emissionSubstances);
}
