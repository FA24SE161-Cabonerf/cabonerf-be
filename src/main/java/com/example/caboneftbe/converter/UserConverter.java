package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.UserDto;
import com.example.caboneftbe.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserDto fromUserToUserDto(User user);
}
