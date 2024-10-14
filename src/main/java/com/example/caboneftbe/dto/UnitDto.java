package com.example.caboneftbe.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.caboneftbe.models.Unit}
 */
@Value
public class UnitDto implements Serializable {
    Long id;
    String name;
    double conversionFactor;
    boolean isDefault;
    UnitGroupDto unitGroup;
}