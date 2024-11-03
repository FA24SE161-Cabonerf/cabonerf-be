package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LoginResponse extends AuthenticationResponse{
    private UserDto user;
}
