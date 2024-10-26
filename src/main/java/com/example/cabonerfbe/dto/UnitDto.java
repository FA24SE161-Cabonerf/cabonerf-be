package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitDto {
    private UUID id;
    private String name;
    private double conversionFactor;
    private boolean isDefault;
    private UnitGroupDto unitGroup;

}
