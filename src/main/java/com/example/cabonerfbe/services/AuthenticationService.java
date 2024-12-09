package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UserDto;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.AuthenticationResponse;
import com.example.cabonerfbe.response.LoginResponse;
import com.example.cabonerfbe.response.RegisterResponse;
import com.example.cabonerfbe.response.ResponseObject;

import java.util.UUID;

public interface AuthenticationService {
    LoginResponse loginByEmail(LoginByEmailRequest request);

    RegisterResponse register(RegisterRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    ResponseObject logout(LogoutRequest request, UUID userId);

    LoginResponse verifyEmail(VerifyEmailRequest request);

    void saveRefreshToken(String refreshTokenString, Users user);

    UserDto changePassword(UUID userId, ChangePasswordRequest request);
}
