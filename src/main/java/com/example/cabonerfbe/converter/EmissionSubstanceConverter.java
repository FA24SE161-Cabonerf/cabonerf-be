package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.EmissionSubstancesDto;
import com.example.cabonerfbe.models.EmissionSubstances;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmissionSubstanceConverter {
    EmissionSubstanceConverter INSTANCE = Mappers.getMapper(EmissionSubstanceConverter.class);

    EmissionSubstancesDto fromEmissionSubstancesToEmissionSubstancesDto(EmissionSubstances emissionSubstances);
}
