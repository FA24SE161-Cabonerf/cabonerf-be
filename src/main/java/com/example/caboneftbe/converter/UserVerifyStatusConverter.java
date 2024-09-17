package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.UserDto;
import com.example.caboneftbe.dto.UserVerifyStatusDto;
import com.example.caboneftbe.models.UserVerifyStatus;
import com.example.caboneftbe.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserVerifyStatusConverter {
    UserVerifyStatusConverter INSTANCE = Mappers.getMapper(UserVerifyStatusConverter.class);

    UserVerifyStatusDto fromUserVerifyStatusToUserVerifyStatusDto(UserVerifyStatus userVerifyStatus);
}
