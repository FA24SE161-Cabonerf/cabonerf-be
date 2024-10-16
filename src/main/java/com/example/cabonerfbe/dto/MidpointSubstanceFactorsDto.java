package com.example.cabonerfbe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MidpointSubstanceFactorsDto {
    private Long id;
    private String casNumber;
    private String name;
    private String chemicalName;
    private String compartmentName;
    private String molecularFormula;
    private String alternativeFormula;
    private Double individualist;
    private Double hierarchist;
    private Double egalitarian;
}
