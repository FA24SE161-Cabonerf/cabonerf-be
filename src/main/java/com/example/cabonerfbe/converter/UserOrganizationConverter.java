package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
import com.example.cabonerfbe.dto.UserOrganizationDto;
import com.example.cabonerfbe.models.UserOrganization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserOrganizationConverter {
    UserOrganizationConverter INSTANCE = Mappers.getMapper(UserOrganizationConverter.class);

    InviteUserOrganizationDto modelToDto(UserOrganization uo);

    UserOrganizationDto enityToDto(UserOrganization uo);
}
