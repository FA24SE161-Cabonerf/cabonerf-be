package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.RoleDto;
import com.example.caboneftbe.dto.UserDto;
import com.example.caboneftbe.models.Role;
import com.example.caboneftbe.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleConverter {
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    RoleDto fromRoleToRoleDto(Role role);
}
