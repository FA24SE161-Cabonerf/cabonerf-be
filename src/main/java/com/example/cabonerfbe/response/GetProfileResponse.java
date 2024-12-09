package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.RoleDto;
import com.example.cabonerfbe.dto.UserVerifyStatusDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetProfileResponse {
    UUID id;
    String fullName;
    String email;
    String phone;
    String profilePictureUrl;
    String bio;
    RoleDto role;
    UserVerifyStatusDto userVerifyStatus;
}
