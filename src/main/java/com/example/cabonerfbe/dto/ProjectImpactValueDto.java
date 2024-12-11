package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Project impact value dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectImpactValueDto {
    private UUID id;
    private BigDecimal value;
    private ImpactMethodCategoryDto impactMethodCategory;
}
