package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.*;

public interface AuthenticationService {
    LoginResponse loginByEmail(LoginByEmailRequest request);
    RegisterResponse register(RegisterRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
    ResponseObject logout(LogoutRequest request, String access_token);
    LoginResponse verifyEmail(VerifyEmailRequest request);
}
