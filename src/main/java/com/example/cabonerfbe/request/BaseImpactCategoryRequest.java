package com.example.cabonerfbe.request;

import com.example.cabonerfbe.models.EmissionCompartment;
import com.example.cabonerfbe.models.MidpointImpactCategory;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private long midpointImpactCategoryId;
    @NotNull(message = "Emission compartment id is required.")
    private long emissionCompartmentId;
}
