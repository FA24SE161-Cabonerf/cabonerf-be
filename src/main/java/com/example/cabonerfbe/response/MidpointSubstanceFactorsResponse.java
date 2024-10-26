package com.example.cabonerfbe.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MidpointSubstanceFactorsResponse {
    private UUID id;
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
