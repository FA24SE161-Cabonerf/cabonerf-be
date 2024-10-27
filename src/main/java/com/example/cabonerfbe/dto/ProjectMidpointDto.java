package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectMidpointDto {
    private UUID id;
    private String name;
    private String abbr;
    private UnitProjectImpactDto unit;
}
