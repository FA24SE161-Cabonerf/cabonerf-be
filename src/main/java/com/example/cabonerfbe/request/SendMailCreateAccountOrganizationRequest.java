package com.example.cabonerfbe.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The class Send mail create account organization response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SendMailCreateAccountOrganizationRequest {
    private String email;
    private String password;
}
