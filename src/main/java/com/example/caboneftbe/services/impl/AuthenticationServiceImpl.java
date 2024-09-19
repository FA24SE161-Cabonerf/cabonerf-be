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
import com.example.caboneftbe.services.MailService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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
    MailService mailService;

    @Autowired
    EmailVerificationTokenService emailVerificationTokenService;

    @Autowired
    EmailVerificationTokenRepository verificationTokenRepository;

    public static final String EMAIL_PASSWORD_WRONG = "Email or password wrong!";

    @Override
    public LoginResponse loginByEmail(LoginByEmailRequest request) {
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
    public RegisterResponse register(RegisterRequest request) {

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
//        if (saved.isPresent()) {
//            try {
//                Random random = new Random();
//                int _token = 100000 + random.nextInt(900000);
//                String token = String.valueOf(100000 + random.nextInt(900000));
//                emailVerificationTokenService.save(saved.get(), token);
//
//                MailRequest mailRequest = new MailRequest();
//                mailRequest.setSubject("Verification Code");
//                mailRequest.setName("Cabonerf");
//                mailRequest.setTo(user.getEmail());
//                mailRequest.setFrom("cabonerf@gmail.com");
//
//                Map<String,Object> model = new HashMap<>();
//                model.put("code", token);
//
////                mailService.sendMailRegister(mailRequest,model);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        var accessToken = jwtService.generateToken(saved.get());
        var refreshToken = jwtService.generateRefreshToken(saved.get());

        return RegisterResponse.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .user(UserConverter.INSTANCE.fromUserToUserDto(saved.get()))
                .build();
    }


    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String clientToken = request.getRefreshToken();
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> CustomExceptions.notFound("User not found!"));
        if (!jwtService.isTokenValid(clientToken, user)) {
            throw CustomExceptions.unauthorized("Invalid or expired refresh token");
        }

        return AuthenticationResponse.builder()
                .access_token(jwtService.generateToken(user))
                .refresh_token(rotateRefreshToken(clientToken, user))
                .build();
    }

    @Override
    public ActiveCodeResponse activeCode(ActiveUserRequest request) {
        EmailVerificationToken token = verificationTokenRepository.findByToken(request.getCode());
        if (token == null) {
            throw CustomExceptions.notFound("Invalid verification code");
        }

        Users users = token.getUsers();

        Optional<Users> _user = userRepository.findById(request.getUserId());
        if (_user.isEmpty()) {
            throw CustomExceptions.notFound("User not found");
        }

        if (_user.equals(users)) {
            throw CustomExceptions.badRequest("Token does not belong to this user");
        }

        if (users.getUserVerifyStatus().getId() != 1) {
            throw CustomExceptions.badRequest("User is verify");
        }

        Timestamp time = new Timestamp(System.currentTimeMillis());
        if (token.getExpiryDate().before(time)) {
            throw CustomExceptions.badRequest("Token has expired");
        }

        users.setUserVerifyStatus(userVerifyStatusRepository.getReferenceById(2L));
        return ActiveCodeResponse.builder()
                .user(UserConverter.INSTANCE.fromUserToUserDto(userRepository.save(users)))
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

        try {
            if(!checkTokenValidity(access_token)){
                throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token format is wrong"));
            }
        } catch (JwtException e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token format is wrong"));
        }
//        if(!isBase64Url(access_token)){
//            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token format is wrong"));
//        }
        String refresh_token = "";

        refresh_token = request.getRefreshToken().substring(7);
        try {
           String userEmail = jwtService.extractUsername(refresh_token);
        } catch (JwtException e) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("refreshToken", "Refresh token format is wrong"));
        }
        try {
            if (jwtService.isTokenExpired(access_token)) {
                throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token is expired"));
            }
        } catch (Exception e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token is expired"));
        }
        Optional<RefreshToken> _refresh_token = refreshTokenRepository.findByToken(refresh_token);
        var user = userRepository.findById(_refresh_token.get().getUsers().getId()).get();
        try {
            if (!jwtService.isTokenValid(access_token, user)) {
                throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token not valid"));
            }
        } catch (Exception e) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Access token not valid"));
        }
        try {
            if (jwtService.isTokenExpired(refresh_token)) {
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

    public static boolean isValidJwtFormat(String token) {
        // Kiểm tra token có đủ 3 phần (header, payload, signature) không
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false; // Token không đủ 3 phần
        }

        // Kiểm tra từng phần có phải là Base64 URL hợp lệ không
        return isBase64Url(parts[0]) && isBase64Url(parts[1]) && isBase64Url(parts[2]);
    }

    // Kiểm tra phần chuỗi có phải Base64 URL không
    private static boolean isBase64Url(String str) {
        try {
            // Decode chuỗi Base64 URL mà không cần padding (=)
            Base64.getUrlDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false; // Không phải chuỗi Base64 URL hợp lệ
        }
    }

    // Hàm kiểm tra token hợp lệ hay không
    public static boolean checkTokenValidity(String token) {
        if (!isValidJwtFormat(token)) {
            return false;
        } else {
            return true;
        }
    }

}
