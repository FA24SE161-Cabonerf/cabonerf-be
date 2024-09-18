package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.request.ActiveUserRequest;
import com.example.caboneftbe.request.LoginByEmailRequest;
import com.example.caboneftbe.request.RefreshTokenRequest;
import com.example.caboneftbe.request.RegisterRequest;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION)
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
                new ResponseObject("success", "Login successfully", authenticationService.loginByEmail(request))
        );
    }

    @PostMapping(API_PARAMS.REGISTER)
    public ResponseEntity<ResponseObject> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject("success", "Register successfully", authenticationService.register(request))
        );
    }

    @PostMapping(API_PARAMS.REFRESH_TOKEN)
    public ResponseEntity<ResponseObject> refreshToken(@RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject("success", "Refresh token successfully.", authenticationService.refreshToken(request))
        );
    }

    @PutMapping(API_PARAMS.ACTIVE_USER)
    public ResponseEntity<ResponseObject> activeUser(@RequestBody ActiveUserRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject("success","Active user successfully", authenticationService.activeCode(request))
        );
    }

}
