package com.example.caboneftbe.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.caboneftbe.models.UnitGroup}
 */
@Value
public class UnitGroupDto implements Serializable {
    Long id;
    String name;
    String unitGroupType;
}