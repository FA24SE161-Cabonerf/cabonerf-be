package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.response.GetProfileResponse;
import com.example.cabonerfbe.response.UpdateAvatarUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserDto fromUserToUserDto(Users users);

    GetProfileResponse fromUserProfileDtoToGetProfileResponse(UserProfileDto userProfileDto);

    UserProfileDto fromUserToUserProfileDto(Users user);

    OwnerDto fromUserToOwnerDto(Users user);

    UserAdminDto forAdmin(Users users);

    UpdateAvatarUserResponse forUpdateAvatar(Users users);

    UserInviteDto forInvite(Users users);
}
