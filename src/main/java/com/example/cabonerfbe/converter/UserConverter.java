package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UserDto;
import com.example.cabonerfbe.dto.UserProfileDto;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.response.GetProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserDto fromUserToUserDto(Users users);

    GetProfileResponse fromUserProfileDtoToGetProfileResponse(UserProfileDto userProfileDto);

    UserProfileDto fromUserToUserProfileDto(Users user);
}
