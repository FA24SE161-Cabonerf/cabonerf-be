package com.example.cabonerfbe.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The class Send mail create account response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SendMailCreateAccountResponse {
    private String token;
    private String email;
    private String password;
}
