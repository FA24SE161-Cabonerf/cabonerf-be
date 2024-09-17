package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.Role;
import com.example.caboneftbe.models.UserVerifyStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserDto {
    long id;
    String userName;
    String email;
//    String phone;
    String profilePictureUrl;
//    String bio;
    RoleDto role;
    UserVerifyStatusDto userVerifyStatus;
}
