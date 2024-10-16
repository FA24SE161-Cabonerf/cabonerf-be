package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UserVerifyStatusDto;
import com.example.cabonerfbe.models.UserVerifyStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserVerifyStatusConverter {
    UserVerifyStatusConverter INSTANCE = Mappers.getMapper(UserVerifyStatusConverter.class);

    UserVerifyStatusDto fromUserVerifyStatusToUserVerifyStatusDto(UserVerifyStatus userVerifyStatus);
}
