package com.example.caboneftbe.services;

import com.example.caboneftbe.request.*;
import com.example.caboneftbe.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthenticationService {
    LoginResponse loginByEmail(LoginByEmailRequest request);
    RegisterResponse register(RegisterRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
    ResponseObject logout(LogoutRequest request, String access_token);
    ResponseObject verifyEmail(VerifyEmailRequest request);
}
