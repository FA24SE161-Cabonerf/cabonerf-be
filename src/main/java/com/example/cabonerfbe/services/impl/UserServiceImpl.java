package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UserConverter;
import com.example.cabonerfbe.dto.UserAdminDto;
import com.example.cabonerfbe.dto.UserProfileDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.response.GetAllUserResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public GetProfileResponse getMe(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, "User not exist"));
        if(Objects.equals(user.getUserStatus().getStatusName(), "Banned")){
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR,"User is banned");
        }
        UserProfileDto userProfileDtoDto = userConverter.fromUserToUserProfileDto(user);

        return userConverter.fromUserProfileDtoToGetProfileResponse(userProfileDtoDto);
    }

    @Override
    public GetAllUserResponse getAll(int pageCurrent, int pageSize) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);
        Page<Users> users = userRepository.findAll(pageable);

        int totalPage = users.getTotalPages();
        if(pageCurrent > totalPage){
            return GetAllUserResponse.builder()
                    .pageCurrent(1)
                    .pageSize(0)
                    .totalPage(0)
                    .users(Collections.emptyList())
                    .build();
        }

        return GetAllUserResponse.builder()
                .pageCurrent(pageCurrent)
                .pageSize(pageSize)
                .totalPage(totalPage)
                .users(users.stream().map(userConverter::forAdmin).collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserAdminDto updateUserStatus(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound("Account not exist"));

        u.setStatus(!u.isStatus());
        return userConverter.forAdmin(userRepository.save(u));
    }
}
