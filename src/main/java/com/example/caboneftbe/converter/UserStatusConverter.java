package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.UserDto;
import com.example.caboneftbe.dto.UserStatusDto;
import com.example.caboneftbe.models.UserStatus;
import com.example.caboneftbe.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserStatusConverter {
    UserStatusConverter INSTANCE = Mappers.getMapper(UserStatusConverter.class);

    UserStatusDto fromUserStatusToUseStatusDto(UserStatus userStatus);
}
