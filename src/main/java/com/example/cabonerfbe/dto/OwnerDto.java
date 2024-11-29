package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

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
