package com.example.cabonerfbe.dto;

import com.example.cabonerfbe.models.SubscriptionType;
import com.example.cabonerfbe.models.UserStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserDto {
    long id;
    String fullName;
    String email;
//    String phone;
    String profilePictureUrl;
//    String bio;
    RoleDto role;
    SubscriptionType subscription;
    UserStatus userStatus;
}
