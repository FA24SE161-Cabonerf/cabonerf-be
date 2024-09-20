package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.UserConverter;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.EmailVerificationToken;
import com.example.caboneftbe.models.RefreshToken;
import com.example.caboneftbe.models.Users;
import com.example.caboneftbe.repositories.*;
import com.example.caboneftbe.request.*;
import com.example.caboneftbe.response.*;
import com.example.caboneftbe.services.EmailVerificationTokenService;
import com.example.caboneftbe.services.JwtService;
import com.example.caboneftbe.services.AuthenticationService;
import com.example.caboneftbe.services.EmailService;
import io.jsonwebtoken.JwtException;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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


    public static final String EMAIL_PASSWORD_WRONG = "Email or password wrong!";

    @Override
    public LoginResponse loginByEmail(LoginByEmailRequest request)  {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()
                -> CustomExceptions.unauthorized(EMAIL_PASSWORD_WRONG, Map.of("password", EMAIL_PASSWORD_WRONG)));
        // flow: lấy pw nhận vào -> encode -> nếu trùng với trong DB -> authen
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuthenticated) {
            throw CustomExceptions.unauthorized(EMAIL_PASSWORD_WRONG, Map.of("password", EMAIL_PASSWORD_WRONG));
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
    public RegisterResponse register(RegisterRequest request){

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("email", "Email already exist."));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("password", "Confirm Passwords do not match."));
        }

        var user = Users.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userStatus(statusRepository.findById(1L).get())
                .userVerifyStatus(userVerifyStatusRepository.findById(1L).get())
                .role(roleRepository.findById(3L).get())
                .subscription(subscriptionTypeRepository.findById(1L).get())
                .status(true)
                .build();

        Optional<Users> saved = Optional.of(userRepository.save(user));
        if (saved.isPresent()) {

            //send mail
            var emailStatusVerify = jwtService.generateEmailVerifyToken(saved.get());
            EmailVerificationToken token = new EmailVerificationToken(emailStatusVerify,null,true,saved.get());
            verificationTokenRepository.save(token);

            emailService.sendVerifyRegisterEmail(saved.get().getEmail(),token.getToken());
        }
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
    public AuthenticationResponse refreshToken(RefreshTokenRequest request){
        String clientToken = request.getRefreshToken();
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> CustomExceptions.notFound("User not found!"));
        if (!jwtService.isTokenValid(clientToken, user,"refresh")) {
            throw CustomExceptions.unauthorized("Invalid or expired refresh token");
        }

        return AuthenticationResponse.builder()
                .access_token(jwtService.generateToken(user))
                .refresh_token(rotateRefreshToken(clientToken, user))
                .build();
    }

    @Override
    public ResponseObject logout(LogoutRequest request, String access_token) {

        if (access_token != null && access_token.startsWith("Bearer ") && access_token.length() > 7) {
            access_token = access_token.substring(7);
        } else if (access_token.isEmpty()) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token is empty"));
        } else {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Invalid access token"));
        }
        String refresh_token = "";

        refresh_token = request.getRefreshToken().substring(7);
        try {
           String userEmail = jwtService.extractUsername(refresh_token,"refresh");
        } catch (JwtException e) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token format is wrong"));
        }
        try {
            if (jwtService.isTokenExpired(access_token,"access")) {
                throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token is expired"));
            }
        } catch (Exception e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token is expired"));
        }
        Optional<RefreshToken> _refresh_token = refreshTokenRepository.findByToken(refresh_token);
        var user = userRepository.findById(_refresh_token.get().getUsers().getId()).get();
        try {
            if (!jwtService.isTokenValid(access_token, user,"access")) {
                throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token not valid"));
            }
        } catch (Exception e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token not valid"));
        }
        try {
            if (jwtService.isTokenExpired(refresh_token,"refresh")) {
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

        if(request.getToken() == null || !request.getToken().startsWith("Bearer ")){
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR,Map.of("Email verify token","Email verify token not valid"));
        }
        var email_verify_token = request.getToken().substring(7);
        try {
            String userEmail = jwtService.extractUsername(email_verify_token,"email_verify");
        } catch (JwtException e) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("emailVerify", "Email verify token format is wrong"));
        }
        try {
            if (jwtService.isTokenExpired(email_verify_token,"email_verify")) {
                throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("emailVerify", "Email verify token is expired"));
            }
        } catch (Exception e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("emailVerify", "Email verify token is expired"));
        }

        EmailVerificationToken token = verificationTokenRepository.findByToken(email_verify_token);

        if(token == null){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR,Map.of("Email verify token","Email verify token not exist"));
        }
        if(!token.isValid()){
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR,Map.of("Email verify token","Email verify token not valid"));
        }

        String email = jwtService.extractUsername(email_verify_token,"email_verify");

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
