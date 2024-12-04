package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class OrganizationIndustryCodeDto {
    private UUID id;
    private IndustryCodeDto industryCode;
}
