package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Organization invite dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class OrganizationInviteDto {
    private UUID id;
    private String name;
    private String logo;
}
