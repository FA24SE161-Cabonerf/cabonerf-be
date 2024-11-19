package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UserInviteDto {
    private UUID id;
    private String fullName;
    private String email;
    private String profilePictureUrl;
}
