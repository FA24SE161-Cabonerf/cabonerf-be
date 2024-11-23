package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseImpactCategoryRequest {
    @NotBlank(message = "Impact category name is required.")
    private String name;
    @NotBlank(message = "Impact category indicator is required.")
    private String indicator;
    @NotBlank(message = "Impact category indicator description is required.")
    private String indicatorDescription;
    @NotBlank(message = "Impact category unit is required.")
    private String unit;
    @NotNull(message = "Midpoint impact category id is required.")
    private UUID midpointImpactCategoryId;
    @NotNull(message = "Emission compartment id is required.")
    private UUID emissionCompartmentId;
}
