package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class User organization dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserOrganizationDto {
    private UUID id;
    private OrganizationInviteDto organization;
}
