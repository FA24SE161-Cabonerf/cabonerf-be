package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UserConverter;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.EmailVerificationToken;
import com.example.cabonerfbe.models.RefreshToken;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.*;
import com.example.cabonerfbe.services.EmailVerificationTokenService;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.services.AuthenticationService;
import com.example.cabonerfbe.services.EmailService;
import io.jsonwebtoken.JwtException;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import java.time.LocalDateTime;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true)
@SuperBuilder
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    SubscriptionTypeRepository subscriptionTypeRepository;

    @Autowired
    UserStatusRepository statusRepository;

    @Autowired
    UserVerifyStatusRepository userVerifyStatusRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    EmailVerificationTokenService emailVerificationTokenService;

    @Autowired
    EmailVerificationTokenRepository verificationTokenRepository;

    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL_FIELD = "email";

    @Override
    public LoginResponse loginByEmail(LoginByEmailRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()
                -> CustomExceptions.unauthorized(MessageConstants.EMAIL_PASSWORD_WRONG, Map.of(PASSWORD_FIELD, MessageConstants.EMAIL_PASSWORD_WRONG)));
        // flow: lấy pw nhận vào -> encode -> nếu trùng với trong DB -> authen
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuthenticated) {
            throw CustomExceptions.unauthorized(MessageConstants.EMAIL_PASSWORD_WRONG, Map.of(PASSWORD_FIELD, MessageConstants.EMAIL_PASSWORD_WRONG));
        }
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveRefreshToken(refreshToken, user);

        return LoginResponse.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .user(UserConverter.INSTANCE.fromUserToUserDto(user))
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of(EMAIL_FIELD, MessageConstants.EMAIL_ALREADY_EXIST));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of(PASSWORD_FIELD, MessageConstants.CONFIRM_PASSWORD_NOT_MATCH));
        }

        var user = Users.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userStatus(statusRepository.findById(1L).get())
                .userVerifyStatus(userVerifyStatusRepository.findById(2L).get())
                .role(roleRepository.findById(4L).get())
                .subscription(subscriptionTypeRepository.findById(1L).get())
                .status(true)
                .build();

        Optional<Users> saved = Optional.of(userRepository.save(user));
        if (saved.isPresent()) {

            //send mail
            var emailStatusVerify = jwtService.generateEmailVerifyToken(saved.get());
            EmailVerificationToken token = new EmailVerificationToken(emailStatusVerify, null, true, saved.get());
            verificationTokenRepository.save(token);

//            emailService.sendVerifyRegisterEmail(saved.get().getEmail(),token.getToken());
        }
        var gatewayToken = jwtService.generateGatewayToken();
        var accessToken = jwtService.generateToken(saved.get());
        var refreshToken = jwtService.generateRefreshToken(saved.get());


        saveRefreshToken(refreshToken, user);

        return RegisterResponse.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .user(UserConverter.INSTANCE.fromUserToUserDto(saved.get()))
                .build();
    }


    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String clientToken = request.getRefreshToken();
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));
        if (!jwtService.isTokenValid(clientToken, user, Constants.TOKEN_TYPE_REFRESH)) {
            throw CustomExceptions.unauthorized(MessageConstants.INVALID_REFRESH_TOKEN);
        }

        return AuthenticationResponse.builder()
                .access_token(jwtService.generateToken(user))
                .refresh_token(rotateRefreshToken(clientToken, user))
                .build();
    }

    @Override
    public ResponseObject logout(LogoutRequest request, String userId) {

        long user_id = Long.parseLong(userId);
        String refresh_token = "";

        refresh_token = request.getRefreshToken().substring(7);
        try {
            String userEmail = jwtService.extractUsername(refresh_token, Constants.TOKEN_TYPE_REFRESH);
        } catch (JwtException e) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token format is wrong"));
        }
        Optional<RefreshToken> _refresh_token = refreshTokenRepository.findByToken(refresh_token);
        var user = userRepository.findById(_refresh_token.get().getUsers().getId()).get();
        if(user.getId() != user_id) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token does not belong to user with id " + userId));
        }
        try {
            if (jwtService.isTokenExpired(refresh_token, "refresh")) {
                throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token is expired"));
            }
        } catch (Exception e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token is expired"));
        }
        try {
            if (!_refresh_token.get().isValid()) {
                throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token not valid"));
            }
        } catch (Exception e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token not valid"));
        }


        _refresh_token.get().setValid(false);
        refreshTokenRepository.save(_refresh_token.get());
        return new ResponseObject("Success", "You have been logged out successfully", "");
    }

    @Override
    public LoginResponse verifyEmail(VerifyEmailRequest request) {

        if (request.getToken() == null || !request.getToken().startsWith("Bearer ")) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("Email verify token", "Email verify token not valid"));
        }
        var email_verify_token = request.getToken().substring(7);
        try {
            String userEmail = jwtService.extractUsername(email_verify_token, "email_verify");
        } catch (JwtException e) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("emailVerify", "Email verify token format is wrong"));
        }
        try {
            if (jwtService.isTokenExpired(email_verify_token, "email_verify")) {
                throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("emailVerify", "Email verify token is expired"));
            }
        } catch (Exception e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("emailVerify", "Email verify token is expired"));
        }

        EmailVerificationToken token = verificationTokenRepository.findByToken(email_verify_token);

        if (token == null) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, Map.of("Email verify token", "Email verify token not exist"));
        }
        if (!token.isValid()) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("Email verify token", "Email verify token not valid"));
        }

        String email = jwtService.extractUsername(email_verify_token, "email_verify");

        var user = userRepository.findByEmail(email).get();
        token.setValid(false);
        verificationTokenRepository.save(token);
        user.setUserVerifyStatus(userVerifyStatusRepository.findById(2L).get());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findTopByTokenOrderByCreatedAtDesc(user.getId());
        refreshToken.get().setValid(false);
        refreshTokenRepository.save(refreshToken.get());

        var access_token = jwtService.generateToken(user);
        var refresh_token = jwtService.generateRefreshToken(user);

        saveRefreshToken(refresh_token,user);

        userRepository.save(user);
        return LoginResponse.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .user(UserConverter.INSTANCE.fromUserToUserDto(user))
                .build();
    }

    private static RefreshToken createRefreshTokenEntity(String refreshToken, Users user) {
        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);
        token.setCreatedAt(LocalDateTime.now());
        token.setValid(true);
        token.setUsers(user);
        return token;
    }

    public void saveRefreshToken(String refreshTokenString, Users user) {
        refreshTokenRepository.save(createRefreshTokenEntity(refreshTokenString, user));
    }

    public String rotateRefreshToken(String oldRefreshTokenString, Users user) {
        // invalidate token cũ
        refreshTokenRepository.findByToken(oldRefreshTokenString).get().setValid(false);
        // gen token string mới
        String newRefreshTokenString = jwtService.generateRefreshToken(user);
        // save token string mới vào db
        saveRefreshToken(newRefreshTokenString, user);
        return newRefreshTokenString;
    }

}
