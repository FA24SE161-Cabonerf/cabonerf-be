package com.example.caboneftbe.services;

import com.example.caboneftbe.request.LoginByEmailRequest;
import com.example.caboneftbe.request.RefreshTokenRequest;
import com.example.caboneftbe.response.AuthenticationResponse;
import com.example.caboneftbe.request.RegisterRequest;
import com.example.caboneftbe.response.LoginResponse;
import com.example.caboneftbe.response.RegisterResponse;

public interface UserService {
    LoginResponse loginByEmail(LoginByEmailRequest request);
    RegisterResponse register(RegisterRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
}
