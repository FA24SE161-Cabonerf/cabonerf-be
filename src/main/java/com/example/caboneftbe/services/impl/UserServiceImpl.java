package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.UserConverter;
import com.example.caboneftbe.exception.GlobalExceptionHandler;
import com.example.caboneftbe.repositories.UserRepository;
import com.example.caboneftbe.request.LoginByEmailRequest;
import com.example.caboneftbe.response.LoginResponse;
import com.example.caboneftbe.services.JwtService;
import com.example.caboneftbe.services.UserService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
