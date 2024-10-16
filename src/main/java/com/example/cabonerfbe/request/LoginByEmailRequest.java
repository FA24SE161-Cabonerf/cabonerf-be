package com.example.cabonerfbe.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginByEmailRequest {
    @NotEmpty(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    String email;
    @NotEmpty(message = "Password is required.")
    String password;
}
