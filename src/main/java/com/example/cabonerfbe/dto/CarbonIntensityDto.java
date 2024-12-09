package com.example.cabonerfbe.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class CarbonIntensityDto {
    private UUID id;
    private String category;
    private BigDecimal value;
    private String unit;
    private String description;
    private String icon;
    private String ref;
    private String ref_description;
}
