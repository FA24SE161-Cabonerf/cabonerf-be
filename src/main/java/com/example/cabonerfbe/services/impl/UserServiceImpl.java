package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UserConverter;
import com.example.cabonerfbe.dto.UserProfileDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.response.GetProfileResponse;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.services.UserService;
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
    public GetProfileResponse getMe(String userId) {
        long user_id = Integer.parseInt(userId);
        var user = userRepository.findById(user_id)
                .orElseThrow(() -> CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, "User not exist"));
        if(user.getUserStatus().getId() == 3){
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR,"User is banned");
        }
        UserProfileDto userProfileDtoDto = userConverter.fromUserToUserProfileDto(user);

        return userConverter.fromUserProfileDtoToGetProfileResponse(userProfileDtoDto);
    }
}
