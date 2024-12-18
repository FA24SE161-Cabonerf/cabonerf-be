package com.example.cabonerfbe.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * The class Refresh token request.
 *
 * @author SonPHH.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenRequest {
    String refreshToken;
    UUID userId;
}
