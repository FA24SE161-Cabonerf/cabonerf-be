package com.example.caboneftbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImpactCategoryDto {
    private String id;
    private String name;
    private String indicator;
    private String indicatorDescription;
    private String unit;

    private MidpointImpactCategoryDto midpointImpactCategory;
    private EmissionCompartmentDto emissionCompartment;
}
