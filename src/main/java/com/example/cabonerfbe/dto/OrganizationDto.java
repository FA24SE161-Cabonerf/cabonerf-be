package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class OrganizationDto {
    UUID id;
    private String name;
    private String description;
    private String taxCode;
    private List<IndustryCodeDto> industryCodes;
    private String logo;
    private ContractDto contract;

}
