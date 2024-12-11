package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.UUID;

/**
 * The class Midpoint impact category dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MidpointImpactCategoryDto {
    private UUID id;
    private String name;
    private String description;
    private String abbr;
    private UnitDto unit;
}