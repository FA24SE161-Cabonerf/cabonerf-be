package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Unit group dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitGroupDto {
    private UUID id;
    private String name;
    private String unitGroupType;
}
