package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Get organization by user dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class GetOrganizationByUserDto {
    private UUID id;
    private String name;
    private String logo;
    private boolean isDefault;
}
