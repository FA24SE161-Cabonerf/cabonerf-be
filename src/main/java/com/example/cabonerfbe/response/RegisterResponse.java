package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The class Register response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RegisterResponse extends AuthenticationResponse {
    private UserDto user;
}
