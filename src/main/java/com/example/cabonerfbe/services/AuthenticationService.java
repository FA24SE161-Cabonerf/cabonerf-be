package com.example.cabonerfbe.services;

import com.example.cabonerfbe.dto.UserDto;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.AuthenticationResponse;
import com.example.cabonerfbe.response.LoginResponse;
import com.example.cabonerfbe.response.RegisterResponse;
import com.example.cabonerfbe.response.ResponseObject;

import java.util.UUID;

/**
 * The interface Authentication service.
 *
 * @author SonPHH.
 */
public interface AuthenticationService {
    /**
     * Login by email method.
     *
     * @param request the request
     * @return the login response
     */
    LoginResponse loginByEmail(LoginByEmailRequest request);

    /**
     * Register method.
     *
     * @param request the request
     * @return the register response
     */
    RegisterResponse register(RegisterRequest request);

    /**
     * Refresh token method.
     *
     * @param request the request
     * @return the authentication response
     */
    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    /**
     * Logout method.
     *
     * @param request the request
     * @param userId  the user id
     * @return the response object
     */
    ResponseObject logout(LogoutRequest request, UUID userId);

    /**
     * Verify email method.
     *
     * @param request the request
     * @return the login response
     */
    LoginResponse verifyEmail(VerifyEmailRequest request);

    /**
     * Save refresh token method.
     *
     * @param refreshTokenString the refresh token string
     * @param user               the user
     */
    void saveRefreshToken(String refreshTokenString, Users user);

    /**
     * Change password method.
     *
     * @param userId  the user id
     * @param request the request
     * @return the user dto
     */
    UserDto changePassword(UUID userId, ChangePasswordRequest request);
}
