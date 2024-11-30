package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class CreateOrganizationDto {
    UUID id;
    private String name;
    private String logo;
    private ContractDto contract;
    private UUID newUserId;
}
