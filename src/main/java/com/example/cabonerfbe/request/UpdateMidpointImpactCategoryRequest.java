package com.example.cabonerfbe.request;

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
public class UpdateMidpointImpactCategoryRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotEmpty(message = "Description is required.")
    private String description;
    @NotEmpty(message = "Abbr is required.")
    private String abbr;
    @NotNull(message = "Unit is required.")
    private UUID unitId;
}
