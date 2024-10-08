package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;

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
    private String emissionCompartment;
//    @ManyToOne
//    @JoinColumn(name = "resource_type_id")
//    private String resourceType;


}
