package com.example.cabonerfbe.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuthenticationResponse {
    private String access_token;
    private String refresh_token;
}
