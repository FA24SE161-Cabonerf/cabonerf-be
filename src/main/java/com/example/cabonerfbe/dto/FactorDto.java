package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class FactorDto {
    UUID id;
    ImpactMethodCategoryDto impactMethodCategory;
    String scientificValue;
    BigDecimal decimalValue;
}
