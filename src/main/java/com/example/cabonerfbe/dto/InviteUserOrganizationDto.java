package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class InviteUserOrganizationDto {
    private UserAdminDto user;
    private RoleDto role;
    private boolean hasJoined;
}
