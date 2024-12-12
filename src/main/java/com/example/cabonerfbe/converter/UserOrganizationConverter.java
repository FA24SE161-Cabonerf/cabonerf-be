package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
import com.example.cabonerfbe.dto.UserOrganizationDto;
import com.example.cabonerfbe.models.UserOrganization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface User organization converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface UserOrganizationConverter {
    /**
     * The constant INSTANCE.
     */
    UserOrganizationConverter INSTANCE = Mappers.getMapper(UserOrganizationConverter.class);

    /**
     * Model to dto method.
     *
     * @param uo the uo
     * @return the invite user organization dto
     */
    InviteUserOrganizationDto modelToDto(UserOrganization uo);

    /**
     * Enity to dto method.
     *
     * @param uo the uo
     * @return the user organization dto
     */
    UserOrganizationDto enityToDto(UserOrganization uo);
}
