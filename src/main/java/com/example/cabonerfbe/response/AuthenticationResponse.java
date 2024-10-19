package com.example.cabonerfbe.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuthenticationResponse {
    private String gateway_token;
    private String access_token;
    private String refresh_token;
}
