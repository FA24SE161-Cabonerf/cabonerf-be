package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.RoleDto;
import com.example.cabonerfbe.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface Role converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {
    /**
     * The constant INSTANCE.
     */
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    /**
     * From role to role dto method.
     *
     * @param role the role
     * @return the role dto
     */
    RoleDto fromRoleToRoleDto(Role role);
}
