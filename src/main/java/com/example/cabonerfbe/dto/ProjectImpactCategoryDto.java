package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Project impact category dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectImpactCategoryDto {
    private UUID id;
    private String name;
    private String iconUrl;

    private ProjectMidpointDto midpointImpactCategory;
    private EmissionCompartmentDto emissionCompartment;
}
