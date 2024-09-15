package com.example.caboneftbe.services;

import com.example.caboneftbe.request.LoginByEmailRequest;
import com.example.caboneftbe.request.RefreshTokenRequest;
import com.example.caboneftbe.response.AuthenticationResponse;
import com.example.caboneftbe.response.LoginResponse;

public interface UserService {
    LoginResponse loginByEmail(LoginByEmailRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);
}
