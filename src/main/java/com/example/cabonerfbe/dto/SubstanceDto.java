package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SubstanceDto {
    private UUID id;
    private String name;
    private String chemicalName;
    private String molecularFormula;
    private String alternativeFormula;
    private String cas;
}
