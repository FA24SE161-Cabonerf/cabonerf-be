package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class User admin dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserAdminDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String profilePictureUrl;
    private String bio;
    private boolean status;
}
