package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserProfileDto {
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
