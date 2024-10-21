package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.USERS)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(API_PARAMS.ME)
    public ResponseEntity<ResponseObject> getMe(@RequestHeader("Authorization") String token) {
        if (!token.startsWith("Bearer ") || token.isEmpty()) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("accessToken", "Invalid token"));
        }

        String accessToken = token.substring(7);;

        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get current user successfully", userService.getMe(accessToken))
        );
    }

}