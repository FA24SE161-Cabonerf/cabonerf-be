package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.UserConverter;
import com.example.caboneftbe.exception.GlobalExceptionHandler;
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

import java.util.Optional;
import java.util.UUID;

@Service
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true)
@SuperBuilder
@AllArgsConstructor
public class UserServiceImpl implements UserService {
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
        return null;
    }
}
