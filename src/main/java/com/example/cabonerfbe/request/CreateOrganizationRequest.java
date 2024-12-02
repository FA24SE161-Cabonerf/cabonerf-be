package com.example.cabonerfbe.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

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
    @NotEmpty(message = "Industry code is required.")
    private List<UUID> industryCodeIds;
    @NotEmpty(message = "Tax code is required.")
    private String taxCode;
    @NotEmpty(message = "Description is required.")
    private String description;
}
