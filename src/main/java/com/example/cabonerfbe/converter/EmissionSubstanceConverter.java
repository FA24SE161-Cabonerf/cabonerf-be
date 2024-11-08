package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.SubstanceDto;
import com.example.cabonerfbe.models.Substance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmissionSubstanceConverter {
    EmissionSubstanceConverter INSTANCE = Mappers.getMapper(EmissionSubstanceConverter.class);

    SubstanceDto fromEmissionSubstancesToEmissionSubstancesDto(Substance emissionSubstances);
}
