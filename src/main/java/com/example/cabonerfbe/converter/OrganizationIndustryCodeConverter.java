package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.IndustryCodeDto;
import com.example.cabonerfbe.dto.OrganizationIndustryCodeDto;
import com.example.cabonerfbe.models.IndustryCode;
import com.example.cabonerfbe.models.OrganizationIndustryCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganizationIndustryCodeConverter {
    OrganizationIndustryCodeConverter INSTANCE = Mappers.getMapper(OrganizationIndustryCodeConverter.class);

    OrganizationIndustryCodeDto modelToDto(OrganizationIndustryCode organizationIndustryCode);
}
