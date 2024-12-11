package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.UserVerifyStatusDto;
import com.example.cabonerfbe.models.UserVerifyStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface User verify status converter.
 *
 * @author SonPHH.
 */
@Mapper(componentModel = "spring")
public interface UserVerifyStatusConverter {
    /**
     * The constant INSTANCE.
     */
    UserVerifyStatusConverter INSTANCE = Mappers.getMapper(UserVerifyStatusConverter.class);

    /**
     * From user verify status to user verify status dto method.
     *
     * @param userVerifyStatus the user verify status
     * @return the user verify status dto
     */
    UserVerifyStatusDto fromUserVerifyStatusToUserVerifyStatusDto(UserVerifyStatus userVerifyStatus);
}
