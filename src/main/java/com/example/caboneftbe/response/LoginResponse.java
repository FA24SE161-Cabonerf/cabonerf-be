package com.example.caboneftbe.response;

import com.example.caboneftbe.dto.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LoginResponse extends AuthenticationResponse{
    private UserDto user;
}
