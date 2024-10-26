package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.RoleDto;
import com.example.cabonerfbe.dto.SubstancesCompartmentsDto;
import com.example.cabonerfbe.models.Role;
import com.example.cabonerfbe.models.SubstancesCompartments;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubstancesCompartmentsConverter {
    SubstancesCompartmentsConverter INSTANCE = Mappers.getMapper(SubstancesCompartmentsConverter.class);

    SubstancesCompartmentsDto ToDto(SubstancesCompartments substancesCompartments);
}
