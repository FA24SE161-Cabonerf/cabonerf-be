package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Unit project impact dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitProjectImpactDto {
    private UUID id;
    private String name;
}
