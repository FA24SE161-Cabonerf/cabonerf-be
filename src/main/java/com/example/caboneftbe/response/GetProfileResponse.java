package com.example.caboneftbe.response;

import com.example.caboneftbe.dto.*;
import com.example.caboneftbe.models.SubscriptionType;
import com.example.caboneftbe.models.UserStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetProfileResponse {
    long id;
    String fullName;
    String email;
    String phone;
    String profilePictureUrl;
    String bio;
    RoleDto role;
    UserVerifyStatusDto userVerifyStatus;
    SubscriptionTypeDto subscription;
    UserStatusDto userStatus;
}
