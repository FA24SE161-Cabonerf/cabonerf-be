package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.OrganizationIndustryCodeDto;
import com.example.cabonerfbe.models.OrganizationIndustryCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Organization industry code converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface OrganizationIndustryCodeConverter {
    /**
     * The constant INSTANCE.
     */
    OrganizationIndustryCodeConverter INSTANCE = Mappers.getMapper(OrganizationIndustryCodeConverter.class);

    /**
     * Model to dto method.
     *
     * @param organizationIndustryCode the organization industry code
     * @return the organization industry code dto
     */
    OrganizationIndustryCodeDto modelToDto(OrganizationIndustryCode organizationIndustryCode);
}
