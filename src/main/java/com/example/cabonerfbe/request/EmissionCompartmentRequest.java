package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmissionCompartmentRequest {
    @NotEmpty(message = "Name is required")
    private String name;
    private String description;
}
