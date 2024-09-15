package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.UserConverter;
import com.example.caboneftbe.exception.GlobalExceptionHandler;
import com.example.caboneftbe.models.RefreshToken;
import com.example.caboneftbe.models.Users;
import com.example.caboneftbe.repositories.*;
import com.example.caboneftbe.request.LoginByEmailRequest;
import com.example.caboneftbe.request.RefreshTokenRequest;
import com.example.caboneftbe.response.AuthenticationResponse;
import com.example.caboneftbe.request.RegisterRequest;
import com.example.caboneftbe.response.LoginResponse;
import com.example.caboneftbe.response.RegisterResponse;
import com.example.caboneftbe.services.JwtService;
import com.example.caboneftbe.services.UserService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true)
@SuperBuilder
@AllArgsConstructor
public class AuthenticationServiceImpl implements UserService {
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

    @Override
    public LoginResponse loginByEmail(LoginByEmailRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> GlobalExceptionHandler.notFound("User not found!"));
        // flow: lấy pw nhận vào -> encode -> nếu trùng với trong DB -> authen
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuthenticated) {
            throw GlobalExceptionHandler.unauthorize("Email or password didn't match!");
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
        if (!userRepository.findByEmail(request.getEmail()).isPresent()) {
            if (request.getPassword().equals(request.getConfirmPassword())) {
                var user = Users.builder()
                        .email(request.getEmail())
                        .userName(request.getFullname())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .userStatus(statusRepository.getById(1L))
                        .userVerifyStatus(userVerifyStatusRepository.getById(1L))
                        .role(roleRepository.findById(3L).get())
                        .subscription(null)
                        .status(true)
                        .build();

                Optional<Users> saved = Optional.of(userRepository.save(user));
                if (saved.isPresent()) {
                    try {
                        // String token = UUID.randomUUID().toString();

                        var accessToken = jwtService.generateToken(saved.get());
                        var refreshToken = jwtService.generateRefreshToken(saved.get());

                        return RegisterResponse.builder()
                                .access_token(accessToken)
                                .refresh_token(refreshToken)
                                .user(UserConverter.INSTANCE.fromUserToUserDto(saved.get()))
                                .build();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String clientToken = request.getRefreshToken();
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> GlobalExceptionHandler.notFound("User not found!"));
        if (!jwtService.isTokenValid(clientToken, user)) {
            throw GlobalExceptionHandler.unauthorize("Invalid or expired refresh token");
        }

        return AuthenticationResponse.builder()
                .access_token(jwtService.generateToken(user))
                .refresh_token(rotateRefreshToken(clientToken, user))
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
