package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping(API_PARAMS.API_VERSION + API_PARAMS.USERS)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(API_PARAMS.ME)
    public ResponseEntity<ResponseObject> getMe(@RequestHeader("x-user-id") UUID userId) {
        log.info("Start getMe. userId: {}", userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get current user successfully", userService.getMe(userId))
        );
    }

    @GetMapping(API_PARAMS.ADMIN)
    public ResponseEntity<ResponseObject> getAll(@RequestParam(required = true,defaultValue = "1") int pageCurrent,
                                                 @RequestParam(required = true, defaultValue = "5") int pageSize){
        log.info("Start getAllUser");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Get all user success",userService.getAll(pageCurrent, pageSize))
        );
    }

    @PostMapping(API_PARAMS.ADMIN + API_PARAMS.BAN_UNBAN_USER)
    public ResponseEntity<ResponseObject> updateUserStatus(@PathVariable("userId") UUID userId){
        log.info("Start ban/unban User. Id: {}", userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS,"Ban/UnBan user success", userService.updateUserStatus(userId))
        );
    }

}
