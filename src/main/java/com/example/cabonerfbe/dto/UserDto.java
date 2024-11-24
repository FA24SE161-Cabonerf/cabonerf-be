package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserDto {
    UUID id;
    String fullName;
    String email;
    //    String phone;
    String profilePictureUrl;
    //    String bio;
    RoleDto role;
}
