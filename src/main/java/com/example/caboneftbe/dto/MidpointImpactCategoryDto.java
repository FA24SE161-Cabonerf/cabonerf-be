package com.example.caboneftbe.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.caboneftbe.models.MidpointImpactCategory}
 */
@Value
public class MidpointImpactCategoryDto implements Serializable {
    Long id;
    String name;
    String description;
    String abbr;
    UnitDto unit;
}