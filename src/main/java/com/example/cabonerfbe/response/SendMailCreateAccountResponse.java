package com.example.cabonerfbe.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SendMailCreateAccountResponse {
    private String token;
    private String email;
    private String password;
}
