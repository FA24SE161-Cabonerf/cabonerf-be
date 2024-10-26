package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

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
