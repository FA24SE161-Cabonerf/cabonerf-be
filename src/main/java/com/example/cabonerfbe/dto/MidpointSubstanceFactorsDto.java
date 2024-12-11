package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Midpoint substance factors dto.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MidpointSubstanceFactorsDto {
    private UUID id;
    private String casNumber;
    private String name;
    private String chemicalName;
    private String compartmentName;
    private String molecularFormula;
    private String alternativeFormula;
    private BigDecimal individualist;
    private BigDecimal hierarchist;
    private BigDecimal egalitarian;
}
