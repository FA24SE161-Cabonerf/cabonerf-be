package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

/**
 * The class Upload org logo response.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadOrgLogoResponse {
    private UUID organizationId;
    private String logo;
}
