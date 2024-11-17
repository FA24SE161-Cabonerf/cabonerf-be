package com.example.cabonerfbe.services;

import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.*;

import java.util.UUID;

public interface AuthenticationService {
    LoginResponse loginByEmail(LoginByEmailRequest request);
    RegisterResponse register(RegisterRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
    ResponseObject logout(LogoutRequest request, UUID userId);
    LoginResponse verifyEmail(VerifyEmailRequest request);
    void saveRefreshToken(String refreshTokenString, Users user);
}
