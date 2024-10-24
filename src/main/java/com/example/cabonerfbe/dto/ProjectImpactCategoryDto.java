package com.example.cabonerfbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectImpactCategoryDto {
    private long id;
    private String name;

    private ProjectMidpointDto midpointImpactCategory;
}
