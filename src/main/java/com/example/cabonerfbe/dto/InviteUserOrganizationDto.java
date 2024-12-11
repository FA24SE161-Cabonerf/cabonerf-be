package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Invite user organization dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class InviteUserOrganizationDto {
    private UUID id;
    private UserInviteDto user;
    private RoleDto role;
    private boolean hasJoined;
}
