package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Impact category dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImpactCategoryDto {
    private UUID id;
    private String name;
    private String indicator;
    private String iconUrl;
    private String indicatorDescription;
    private String unit;

    private MidpointImpactCategoryDto midpointImpactCategory;
    private EmissionCompartmentDto emissionCompartment;
}
