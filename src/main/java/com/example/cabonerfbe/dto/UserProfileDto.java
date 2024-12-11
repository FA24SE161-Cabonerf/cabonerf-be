package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class User profile dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserProfileDto {
    /**
     * The Id.
     */
    UUID id;
    /**
     * The Full name.
     */
    String fullName;
    /**
     * The Email.
     */
    String email;
    /**
     * The Phone.
     */
    String phone;
    /**
     * The Profile picture url.
     */
    String profilePictureUrl;
    /**
     * The Bio.
     */
    String bio;
    /**
     * The Role.
     */
    RoleDto role;
    /**
     * The User verify status.
     */
    UserVerifyStatusDto userVerifyStatus;
}
