package com.example.caboneftbe.services;

import com.example.caboneftbe.request.*;
import com.example.caboneftbe.response.ActiveCodeResponse;
import com.example.caboneftbe.response.AuthenticationResponse;
import com.example.caboneftbe.response.LoginResponse;
import com.example.caboneftbe.response.RegisterResponse;

public interface AuthenticationService {
    LoginResponse loginByEmail(LoginByEmailRequest request);
    RegisterResponse register(RegisterRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
    ActiveCodeResponse activeCode(ActiveUserRequest request);
    LoginResponse logout(LogoutRequest request, String access_token);
}
