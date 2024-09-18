package com.example.caboneftbe.services;

import com.example.caboneftbe.request.*;
import com.example.caboneftbe.response.*;

public interface AuthenticationService {
    LoginResponse loginByEmail(LoginByEmailRequest request);
    RegisterResponse register(RegisterRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
    ActiveCodeResponse activeCode(ActiveUserRequest request);
    ResponseObject logout(LogoutRequest request, String access_token);
}
