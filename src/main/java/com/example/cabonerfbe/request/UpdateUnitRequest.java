package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUnitRequest {
    @NotBlank(message = "Unit name cannot be empty")
    private String unitName;

    @Positive(message = "Conversion factor must be a positive number")
    private double conversionFactor;

    @NotNull(message = "isDefault must be specified")
    private Boolean isDefault;

    @NotNull(message = "Unit group ID cannot be null")
    @Positive(message = "Unit group ID must be a positive number")
    private UUID unitGroupId;
}
