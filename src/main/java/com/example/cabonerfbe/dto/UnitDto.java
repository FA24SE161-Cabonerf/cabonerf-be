package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitDto {
    private UUID id;
    private String name;
    private BigDecimal conversionFactor;
    private Boolean isDefault;
    private UnitGroupDto unitGroup;

}
