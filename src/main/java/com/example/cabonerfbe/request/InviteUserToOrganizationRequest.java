package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * The class Invite user to organization request.
 *
 * @author SonPHH.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteUserToOrganizationRequest {

    @NotEmpty(message = "Invite must include at least one user")
    private List<UUID> userIds;
    @NotNull(message = "Organization is required")
    private UUID organizationId;
}
