package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadOrgLogoResponse {
    private UUID organizationId;
    private String logo;
}
