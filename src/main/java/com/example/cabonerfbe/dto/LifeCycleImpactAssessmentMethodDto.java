package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Life cycle impact assessment method dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class LifeCycleImpactAssessmentMethodDto {
    private UUID id;
    private String name;
    private String description;
    private String version;
    private String reference;
    private PerspectiveDto perspective;
}
