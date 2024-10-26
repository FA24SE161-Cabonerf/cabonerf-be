package com.example.cabonerfbe.dto;

import com.example.cabonerfbe.models.EmissionSubstances;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SubstancesCompartmentsDto {
    private long id;
    private EmissionSubstancesDto emissionSubstance;
    private EmissionCompartmentDto emissionCompartment;
}
