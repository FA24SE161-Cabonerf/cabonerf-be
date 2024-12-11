package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Organization industry code dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class OrganizationIndustryCodeDto {
    private UUID id;
    private IndustryCodeDto industryCode;
}
