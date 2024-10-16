package com.example.cabonerfbe.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenRequest {
    String refreshToken;
    Long userId;
}
