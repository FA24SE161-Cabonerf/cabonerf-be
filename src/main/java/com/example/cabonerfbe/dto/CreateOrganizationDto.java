package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The class Create organization dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class CreateOrganizationDto {
    /**
     * The Id.
     */
    UUID id;
    private String name;
    private String description;
    private String taxCode;
    private String logo;
    private ContractDto contract;
    private List<IndustryCodeDto> industryCodes;
    private UUID newUserId;
}
