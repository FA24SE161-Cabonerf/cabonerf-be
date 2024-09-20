package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.UserDto;
import com.example.caboneftbe.models.Users;
import com.example.caboneftbe.response.GetProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserDto fromUserToUserDto(Users users);

    GetProfileResponse fromUserDtoToGetProfileResponse(UserDto userDto);


}
