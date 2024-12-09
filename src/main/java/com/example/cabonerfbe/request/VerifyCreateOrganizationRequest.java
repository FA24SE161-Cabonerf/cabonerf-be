package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCreateOrganizationRequest {
    @NotNull(message = "Organization is required")
    private UUID organizationId;
    @NotNull(message = "Token is required")
    private String token;
}
