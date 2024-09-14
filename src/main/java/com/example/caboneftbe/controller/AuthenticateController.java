package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.request.LoginByEmailRequest;
import com.example.caboneftbe.response.ResponseObject;
import com.example.caboneftbe.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(API_PARAMS.API_VERSION)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AuthenticateController {
    @Autowired
    private UserService userService;

    @PostMapping(API_PARAMS.LOGIN_BY_EMAIL)
    public ResponseEntity<ResponseObject> loginByEmail(@RequestBody LoginByEmailRequest request){
        return ResponseEntity.ok().body(
                new ResponseObject("success", "Login successfully", userService.loginByEmail(request))
        );
    }


}
