package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.USERS)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class AuthenticateController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(API_PARAMS.LOGIN_BY_EMAIL)
    public ResponseEntity<ResponseObject> loginByEmail(@Valid @RequestBody LoginByEmailRequest request) {
        log.info("Start loginByEmail. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Login successfully", authenticationService.loginByEmail(request))
        );
    }

    @PostMapping(API_PARAMS.REGISTER)
    public ResponseEntity<ResponseObject> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Start register. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Register successfully", authenticationService.register(request))
        );
    }

    @PostMapping(API_PARAMS.REFRESH_TOKEN)
    public ResponseEntity<ResponseObject> refreshToken(@RequestBody RefreshTokenRequest request) {
        log.info("Start refreshToken. request: {}", request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Refresh token successfully.", authenticationService.refreshToken(request))
        );
    }

    @PostMapping(API_PARAMS.LOGOUT)
    public ResponseEntity<ResponseObject> logout(@RequestHeader(value = "x-user-id", required = true) UUID userId, @Valid @RequestBody(required = true) LogoutRequest logoutRequest) {
        log.info("Start logout. userID: {}, request: {}", userId, logoutRequest);
        return ResponseEntity.ok().body(
                authenticationService.logout(logoutRequest, userId)
        );
    }

    @PostMapping(API_PARAMS.EMAIL_VERIFY)
    public ResponseEntity<ResponseObject> verify(@RequestParam("token") String token) {
        VerifyEmailRequest request = new VerifyEmailRequest(token);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Verify email successfully.", authenticationService.verifyEmail(request))
        );
    }

    @PutMapping(API_PARAMS.CHANGE_PASSWORD)
    public ResponseEntity<ResponseObject> changePassword(@RequestHeader(value = "x-user-id", required = true) UUID userId,
                                                         @Valid @RequestBody ChangePasswordRequest request) {
        log.info("Start changePassword. userId: {}, request: {}", userId, request);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.CHANGE_PASSWORD_SUCCESS, authenticationService.changePassword(userId, request))
        );
    }


}
