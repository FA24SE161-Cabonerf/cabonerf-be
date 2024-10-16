package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UserStatusDto;
import com.example.cabonerfbe.models.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserStatusConverter {
    UserStatusConverter INSTANCE = Mappers.getMapper(UserStatusConverter.class);

    UserStatusDto fromUserStatusToUseStatusDto(UserStatus userStatus);
}
