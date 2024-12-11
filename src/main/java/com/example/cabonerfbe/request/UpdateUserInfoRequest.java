package com.example.cabonerfbe.request;

import lombok.*;

/**
 * The class Update user info request.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserInfoRequest {
    /**
     * The Full name.
     */
    String fullName;
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
}
