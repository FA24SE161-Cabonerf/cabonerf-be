package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.request.*;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.AuthenticationService;
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
    public ResponseEntity<ResponseObject> loginByEmail(@Valid @RequestBody LoginByEmailRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Login successfully", authenticationService.loginByEmail(request))
        );
    }

    @PostMapping(API_PARAMS.REGISTER)
    public ResponseEntity<ResponseObject> register(@Valid @RequestBody RegisterRequest request){
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

    @PutMapping(API_PARAMS.ACTIVE_USER)
    public ResponseEntity<ResponseObject> activeUser(@RequestBody ActiveUserRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Active user successfully", authenticationService.activeCode(request))
        );
    }

    @PostMapping(API_PARAMS.LOGOUT)
    public ResponseEntity<ResponseObject> logout(@RequestHeader("Authorization") String accessToken, @RequestBody LogoutRequest logoutRequest){
        return ResponseEntity.ok().body(
                authenticationService.logout(logoutRequest,accessToken)
        );
    }
}
