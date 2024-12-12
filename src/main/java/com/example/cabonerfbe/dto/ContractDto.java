package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Contract dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ContractDto {
    private UUID id;
    private String url;
}
