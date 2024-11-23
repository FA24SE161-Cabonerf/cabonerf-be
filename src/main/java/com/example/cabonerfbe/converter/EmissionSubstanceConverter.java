package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.dto.SearchEmissionSubstanceDto;
import com.example.cabonerfbe.models.EmissionSubstance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmissionSubstanceConverter {
    EmissionSubstanceConverter INSTANCE = Mappers.getMapper(EmissionSubstanceConverter.class);

    SearchEmissionSubstanceDto ToDto(EmissionSubstance emissionSubstance);

    EmissionSubstanceDto modelToDto(EmissionSubstance emissionSubstance);

}
