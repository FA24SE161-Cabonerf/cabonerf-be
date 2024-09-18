package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.UserConverter;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.EmailVerificationToken;
import com.example.caboneftbe.models.RefreshToken;
import com.example.caboneftbe.models.Users;
import com.example.caboneftbe.repositories.*;
import com.example.caboneftbe.request.*;
import com.example.caboneftbe.response.ActiveCodeResponse;
import com.example.caboneftbe.response.AuthenticationResponse;
import com.example.caboneftbe.response.LoginResponse;
import com.example.caboneftbe.response.RegisterResponse;
import com.example.caboneftbe.services.EmailVerificationTokenService;
import com.example.caboneftbe.services.JwtService;
import com.example.caboneftbe.services.AuthenticationService;
import com.example.caboneftbe.services.MailService;
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

    @Override
    public LoginResponse loginByEmail(LoginByEmailRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> CustomExceptions.notFound("User not found!"));
        // flow: lấy pw nhận vào -> encode -> nếu trùng với trong DB -> authen
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuthenticated) {
            throw  CustomExceptions.unauthorized("Email or password wrong!");
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
            throw CustomExceptions.badRequest("Email already exists!");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw CustomExceptions.badRequest("Passwords and Confirm Passwords don't match!");
        }

        var user = Users.builder()
                .email(request.getEmail())
                .fullName(request.getFullname())
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
                Random random = new Random();
                int _token = 100000 + random.nextInt(900000);
                String token = String.valueOf(100000 + random.nextInt(900000));
                emailVerificationTokenService.save(saved.get(), token);

                MailRequest mailRequest = new MailRequest();
                mailRequest.setSubject("Verification Code");
                mailRequest.setName("Cabonerf");
                mailRequest.setTo(user.getEmail());
                mailRequest.setFrom("cabonerf@gmail.com");

                Map<String,Object> model = new HashMap<>();
                model.put("code", token);

                mailService.sendMailRegister(mailRequest,model);

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
        return null;
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
        if(token == null){
            throw CustomExceptions.notFound("Invalid verification code");
        }

        Users users = token.getUsers();

        Optional<Users> _user = userRepository.findById(request.getUserId());
        if(_user.isEmpty()){
            throw CustomExceptions.notFound("User not found");
        }

        if(_user.equals(users)){
            throw CustomExceptions.badRequest("Token does not belong to this user");
        }

        if(users.getUserVerifyStatus().getId() != 1){
            throw CustomExceptions.badRequest("User is verify");
        }

        Timestamp time = new Timestamp(System.currentTimeMillis());
        if(token.getExpiryDate().before(time)){
            throw CustomExceptions.badRequest("Token has expired");
        }

        users.setUserVerifyStatus(userVerifyStatusRepository.getReferenceById(2L));
        return ActiveCodeResponse.builder()
                .user(UserConverter.INSTANCE.fromUserToUserDto(userRepository.save(users)))
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
