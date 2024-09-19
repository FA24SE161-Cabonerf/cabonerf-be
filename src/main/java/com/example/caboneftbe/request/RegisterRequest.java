package com.example.caboneftbe.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please enter a valid email address")
    String email;

    @NotBlank(message = "Full Name cannot be empty")
    @Size(min = 3, max = 50, message = "'Full name must contain at least 3 characters and no more than 50 characters")
    @Pattern(regexp = "^[A-ZÀ-Ỹ][A-Za-zÀ-ỹ\\s]*$",
            message = "Full Name must start with a capital letter and not contain special characters or numbers")
    String fullName;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 1 lowercase, 1 uppercase, 1 number and 1 symbol")
    String password;

    String confirmPassword;

}
