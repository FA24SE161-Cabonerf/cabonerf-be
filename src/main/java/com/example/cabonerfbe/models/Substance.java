package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Substance extends Base {
    private String name;
    private String chemicalName;
    private String molecularFormula;
    private String alternativeFormula;
    private String cas;
}
