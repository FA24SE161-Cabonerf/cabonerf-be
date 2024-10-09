package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.EmissionCompartment;
import com.example.caboneftbe.models.EmissionSubstances;
import com.example.caboneftbe.models.ImpactMethodCategory;
import com.example.caboneftbe.models.Unit;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MidpointImpactCharacterizationFactorsDto {
    private long id;
    private ImpactMethodCategoryDto impactMethodCategory;
    private EmissionSubstancesDto emissionSubstances;
    private String scientificValue;
    private double decimalValue;
    private UnitDto unit;
    private EmissionCompartmentDto emissionCompartment;
}
