package com.example.cabonerfbe.request;

import com.example.cabonerfbe.dto.UnitDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMidpointImpactCategoryRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    private String description;
    @NotEmpty(message = "Abbr is required.")
    private String abbr;
    @NotNull(message = "Unit is required.")
    private UUID unitId;
}