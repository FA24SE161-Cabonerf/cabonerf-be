package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.request.LoginByEmailRequest;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(API_PARAMS.API_VERSION)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
public class AuthenticateController {
    @Autowired
    private UserService userService;

    @PostMapping(API_PARAMS.LOGIN_BY_EMAIL)
    public ResponseEntity<ResponseObject> loginByEmail(@Valid @RequestBody LoginByEmailRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject("success", "Login successfully", userService.loginByEmail(request))
        );
    }


}