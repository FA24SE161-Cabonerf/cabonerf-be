package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Owner dto.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OwnerDto {
    private UUID id;
    private String fullName;
    private String email;
    private String profilePictureUrl;
}
