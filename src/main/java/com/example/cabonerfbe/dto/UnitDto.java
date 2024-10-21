package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitDto {
    private long id;
    private String name;
    private double conversionFactor;
    private boolean isDefault;
    private UnitGroupDto unitGroup;

}
