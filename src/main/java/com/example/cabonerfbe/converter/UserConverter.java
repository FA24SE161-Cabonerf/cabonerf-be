package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.response.GetProfileResponse;
import com.example.cabonerfbe.response.UpdateAvatarUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface User converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface UserConverter {
    /**
     * The constant INSTANCE.
     */
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * From user to user dto method.
     *
     * @param users the users
     * @return the user dto
     */
    UserDto fromUserToUserDto(Users users);

    /**
     * From user profile dto to get profile response method.
     *
     * @param userProfileDto the user profile dto
     * @return the get profile response
     */
    GetProfileResponse fromUserProfileDtoToGetProfileResponse(UserProfileDto userProfileDto);

    /**
     * From user to user profile dto method.
     *
     * @param user the user
     * @return the user profile dto
     */
    UserProfileDto fromUserToUserProfileDto(Users user);

    /**
     * From user to owner dto method.
     *
     * @param user the user
     * @return the owner dto
     */
    OwnerDto fromUserToOwnerDto(Users user);

    /**
     * For admin method.
     *
     * @param users the users
     * @return the user admin dto
     */
    UserAdminDto forAdmin(Users users);

    /**
     * For update avatar method.
     *
     * @param users the users
     * @return the update avatar user response
     */
    UpdateAvatarUserResponse forUpdateAvatar(Users users);

    /**
     * For invite method.
     *
     * @param users the users
     * @return the user invite dto
     */
    UserInviteDto forInvite(Users users);
}
