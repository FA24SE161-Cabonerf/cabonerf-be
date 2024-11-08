package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class EmissionSubstances extends Base{
    private String name;
    private String chemicalName;
    private String molecularFormula;
    private String alternativeFormula;
    private String cas;
}
