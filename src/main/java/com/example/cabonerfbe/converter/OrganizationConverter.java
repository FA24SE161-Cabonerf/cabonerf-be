package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.CreateOrganizationDto;
import com.example.cabonerfbe.dto.GetOrganizationByUserDto;
import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.models.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Organization converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface OrganizationConverter {
    /**
     * The constant INSTANCE.
     */
    OrganizationConverter INSTANCE = Mappers.getMapper(OrganizationConverter.class);

    /**
     * Model to dto method.
     *
     * @param organization the organization
     * @return the organization dto
     */
    OrganizationDto modelToDto(Organization organization);

    /**
     * Model to create dto method.
     *
     * @param organization the organization
     * @return the create organization dto
     */
    CreateOrganizationDto modelToCreateDto(Organization organization);

    /**
     * Model to user method.
     *
     * @param organization the organization
     * @return the get organization by user dto
     */
    GetOrganizationByUserDto modelToUser(Organization organization);
}
