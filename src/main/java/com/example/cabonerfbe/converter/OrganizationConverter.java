package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.GetOrganizationByUserDto;
import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.models.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganizationConverter {
    OrganizationConverter INSTANCE = Mappers.getMapper(OrganizationConverter.class);

    OrganizationDto modelToDto(Organization organization);

    GetOrganizationByUserDto modelToUser(Organization organization);
}
