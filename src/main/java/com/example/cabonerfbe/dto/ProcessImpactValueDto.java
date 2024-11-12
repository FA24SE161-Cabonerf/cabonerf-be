package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessImpactValueDto {
    private UUID id;
    private BigDecimal unitLevel;
    private BigDecimal systemLevel;
    private BigDecimal overallImpactContribution;
    private MethodDto method;
    private ProjectImpactCategoryDto impactCategory;
}
