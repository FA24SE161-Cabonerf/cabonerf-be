package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.request.UpdateUserInfoRequest;
import com.example.cabonerfbe.response.ResponseObject;
import com.example.cabonerfbe.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ResponseObject> getAll(@RequestParam(required = true, defaultValue = "1") int pageCurrent,
                                                 @RequestParam(required = true, defaultValue = "5") int pageSize,
                                                 @RequestParam(required = false) String keyword) {
        log.info("Start getAllUser");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get all user success", userService.getAll(pageCurrent, pageSize, keyword))
        );
    }

    @PostMapping(API_PARAMS.ADMIN + API_PARAMS.BAN_UNBAN_USER)
    public ResponseEntity<ResponseObject> updateUserStatus(@PathVariable("userId") UUID userId) {
        log.info("Start ban/unban User. Id: {}", userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Ban/UnBan user success", userService.updateUserStatus(userId))
        );
    }

    @PutMapping(API_PARAMS.UPDATE_AVATAR_USER)
    public ResponseEntity<ResponseObject> updateAvatar(@RequestHeader("x-user-id") UUID userId, @RequestParam("image") MultipartFile image) {
        log.info("Start updateAvatar. id: {}", userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Update avatar user success", userService.updateAvatarUser(userId, image))
        );
    }

    @GetMapping(API_PARAMS.GET_USER_TO_INVITE)
    public ResponseEntity<ResponseObject> getUserToInvite(@RequestParam(defaultValue = "1") int pageCurrent,
                                                          @RequestParam(defaultValue = "5") int pageSize,
                                                          @RequestParam(required = false) String keyword) {
        log.info("Start getUserToInvite");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get user to invite success", userService.getToInvite(pageCurrent, pageSize, keyword))
        );
    }

    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.GET_USER_TO_DASHBOARD)
    public ResponseEntity<ResponseObject> getUserInDashboard() {
        log.info("Start getUserInDashboard");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get user dashboard success", userService.getNewUserInThisYear())
        );
    }

    @GetMapping(API_PARAMS.ADMIN + API_PARAMS.GET_ALL_USER_TO_DASHBOARD)
    public ResponseEntity<ResponseObject> getAllUser() {
        log.info("Start getAllUser");
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, "Get all user dashboard success", userService.countAllUser())
        );
    }

    @PutMapping(API_PARAMS.UPDATE_PROFILE)
    public ResponseEntity<ResponseObject> updateUserProfile(@RequestHeader("x-user-id") UUID userId, @Valid @RequestBody UpdateUserInfoRequest request) {
        log.info("Start updateUserProfile. id: {}", userId);
        return ResponseEntity.ok().body(
                new ResponseObject(Constants.RESPONSE_STATUS_SUCCESS, MessageConstants.UPDATE_USER_PROFILE_SUCCESS, userService.updateProfile(userId, request))
        );
    }


}
