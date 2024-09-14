package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.Role;
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
    String phone;
    String profilePictureUrl;
    String bio;
    Role role;
}
