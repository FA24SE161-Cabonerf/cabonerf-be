package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.UserConverter;
import com.example.caboneftbe.dto.UserDto;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.exception.GlobalExceptionHandler;
import com.example.caboneftbe.repositories.UserRepository;
import com.example.caboneftbe.response.GetProfileResponse;
import com.example.caboneftbe.services.JwtService;
import com.example.caboneftbe.services.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true)
@SuperBuilder
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserConverter userConverter = UserConverter.INSTANCE;

    @Override
    public GetProfileResponse getMe(String accessToken) {
        if (jwtService.isTokenExpired(accessToken)) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token has expired"));
        }

        String email = jwtService.extractUsername(accessToken);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token is not valid")));

        UserDto userDto = userConverter.fromUserToUserDto(user);

        return userConverter.fromUserDtoToGetProfileResponse(userDto);
    }
}
