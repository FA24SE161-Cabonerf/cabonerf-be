package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Factor dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class FactorDto {
    /**
     * The Id.
     */
    UUID id;
    /**
     * The Impact method category.
     */
    ImpactMethodCategoryDto impactMethodCategory;
    /**
     * The Scientific value.
     */
    String scientificValue;
    /**
     * The Decimal value.
     */
    BigDecimal decimalValue;
}
