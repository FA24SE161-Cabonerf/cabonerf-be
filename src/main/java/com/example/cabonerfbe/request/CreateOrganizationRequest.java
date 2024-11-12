package com.example.cabonerfbe.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
public class CreateOrganizationRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotEmpty(message = "Email is required.")
    @Email(message = "Please enter a valid email address")
    private String email;
}
