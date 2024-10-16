package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.RoleDto;
import com.example.cabonerfbe.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleConverter {
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    RoleDto fromRoleToRoleDto(Role role);
}
