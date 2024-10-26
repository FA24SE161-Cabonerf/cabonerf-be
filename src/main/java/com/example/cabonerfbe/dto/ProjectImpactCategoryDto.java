package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

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
}
