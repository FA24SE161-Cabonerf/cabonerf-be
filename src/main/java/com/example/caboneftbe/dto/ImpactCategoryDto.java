package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.EmissionCompartment;
import com.example.caboneftbe.models.MidpointImpactCategory;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImpactCategoryDto {
    private String name;
    private String indicator;
    private String indicatorDescription;
    private String unit;
    private MidpointImpactCategoryDto midpointImpactCategory;
    private EmissionCompartmentDto emissionCompartment;
}
