package com.example.caboneftbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class EmissionSubstancesDto {
    private long id;
    private String name;
    private String chemicalName;
    private String molecularFormula;
    private String alternativeFormula;
}
