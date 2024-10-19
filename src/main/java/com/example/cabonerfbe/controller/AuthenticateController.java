package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.USERS)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class AuthenticateController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(API_PARAMS.LOGIN_BY_EMAIL)
    public ResponseEntity<ResponseObject> loginByEmail(@Valid @RequestBody LoginByEmailRequest request) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Login successfully", authenticationService.loginByEmail(request))
        );
    }

    @PostMapping(API_PARAMS.REGISTER)
    public ResponseEntity<ResponseObject> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Register successfully", authenticationService.register(request))
        );
    }

    @PostMapping(API_PARAMS.REFRESH_TOKEN)
    public ResponseEntity<ResponseObject> refreshToken(@RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Refresh token successfully.", authenticationService.refreshToken(request))
        );
    }

    @PostMapping(API_PARAMS.LOGOUT)
    public ResponseEntity<ResponseObject> logout(@RequestHeader(value = "x-user-id",required = true) String userId,@Valid @RequestBody(required = true) LogoutRequest logoutRequest){
        return ResponseEntity.ok().body(
                authenticationService.logout(logoutRequest,userId)
        );
    }

    @PostMapping(API_PARAMS.EMAIL_VERIFY)
    public ResponseEntity<ResponseObject> verify(@Valid @RequestBody(required = true) VerifyEmailRequest verifyEmailRequest){
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Verify email successfully.", authenticationService.verifyEmail(verifyEmailRequest))
        );
    }


}
