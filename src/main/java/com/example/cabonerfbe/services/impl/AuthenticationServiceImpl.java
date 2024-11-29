package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.UserConverter;
import com.example.cabonerfbe.dto.UserDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.AuthenticationResponse;
import com.example.cabonerfbe.response.LoginResponse;
import com.example.cabonerfbe.response.RegisterResponse;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.AuthenticationService;
import com.example.cabonerfbe.services.EmailService;
import com.example.cabonerfbe.services.EmailVerificationTokenService;
import com.example.cabonerfbe.services.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true)
@SuperBuilder
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    public static final String PASSWORD_FIELD = "password";
    public static final String EMAIL_FIELD = "email";
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
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
    @Autowired
    UserOrganizationRepository uoRepository;
    @Autowired
    OrganizationRepository oRepository;

    private static RefreshToken createRefreshTokenEntity(String refreshToken, Users user) {
        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);
        token.setCreatedAt(LocalDateTime.now());
        token.setValid(true);
        token.setUsers(user);
        return token;
    }

    @Override
    public LoginResponse loginByEmail(LoginByEmailRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()
                -> CustomExceptions.unauthorized(MessageConstants.EMAIL_PASSWORD_WRONG, Map.of(PASSWORD_FIELD, MessageConstants.EMAIL_PASSWORD_WRONG)));
        if (!user.isStatus()) {
            throw CustomExceptions.unauthorized(MessageConstants.USER_IS_BANNED);
        }
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
                .userVerifyStatus(userVerifyStatusRepository.findByName("Verified").get())
                .role(roleRepository.findByName("LCA Staff").get())
                .status(true)
                .build();

        Optional<Users> saved = Optional.of(userRepository.save(user));

        var accessToken = jwtService.generateToken(saved.get());
        var refreshToken = jwtService.generateRefreshToken(saved.get());


        saveRefreshToken(refreshToken, user);
        Organization o = new Organization();
        o.setName("My organization");
        o.setContract(null);
        o.setLogo("");
        o = oRepository.save(o);

        UserOrganization uo = new UserOrganization();
        uo.setUser(user);
        uo.setOrganization(o);
        uo.setHasJoined(true);
        uo.setRole(roleRepository.findByName("Organization Manager").get());
        uoRepository.save(uo);

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
    public ResponseObject logout(LogoutRequest request, UUID userId) {

        String refresh_token = "";

        refresh_token = request.getRefreshToken().substring(7);
        try {
            String userEmail = jwtService.extractUsername(refresh_token, Constants.TOKEN_TYPE_REFRESH);
        } catch (JwtException e) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token format is wrong"));
        }
        Optional<RefreshToken> _refresh_token = refreshTokenRepository.findByToken(refresh_token);
        if (_refresh_token.isEmpty()) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token not exist"));
        }
        Users user = userRepository.findById(_refresh_token.get().getUsers().getId()).get();

        if (!user.getId().equals(userId)) {
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

        EmailVerificationToken _token = jwtService.checkToken(request.getToken());

        String email = _token.getUsers().getEmail();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> CustomExceptions.notFound("Email not exist"));
        if (user.getRole().getName().equals("Verified")) {
            throw CustomExceptions.badRequest("Account already verified");
        }

        user.setUserVerifyStatus(userVerifyStatusRepository.findByName("Verified").get());

        _token.setValid(false);
        verificationTokenRepository.save(_token);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findTopByTokenOrderByCreatedAtDesc(user.getId());
        refreshToken.get().setValid(false);
        refreshTokenRepository.save(refreshToken.get());

        var access_token = jwtService.generateToken(user);
        var refresh_token = jwtService.generateRefreshToken(user);

        saveRefreshToken(refresh_token, user);

        userRepository.save(user);



        return LoginResponse.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .user(UserConverter.INSTANCE.fromUserToUserDto(user))
                .build();
    }

    public void saveRefreshToken(String refreshTokenString, Users user) {
        refreshTokenRepository.save(createRefreshTokenEntity(refreshTokenString, user));
    }

    @Override
    public UserDto changePassword(UUID userId, ChangePasswordRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR,Map.of("Old Password",MessageConstants.PASSWORD_WRONG));
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR,Map.of("New Password",MessageConstants.NEW_PASSWORD_SAME_AS_OLD));
        }
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR,Map.of("Confirm Password",MessageConstants.CONFIRM_PASSWORD_NOT_MATCH));
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return UserConverter.INSTANCE.fromUserToUserDto(userRepository.save(user));
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
