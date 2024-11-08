package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.EmissionSubstanceDto;
import com.example.cabonerfbe.dto.SearchSubstancesCompartmentsDto;
import com.example.cabonerfbe.models.EmissionSubstance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubstancesCompartmentsConverter {
    SubstancesCompartmentsConverter INSTANCE = Mappers.getMapper(SubstancesCompartmentsConverter.class);

    SearchSubstancesCompartmentsDto ToDto(EmissionSubstance substancesCompartments);
    EmissionSubstanceDto modelToDto(EmissionSubstance substancesCompartments);


}
