package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Update unit request.
 *
 * @author SonPHH.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUnitRequest {
    @NotBlank(message = "Unit name cannot be empty")
    private String unitName;

    @Positive(message = "Conversion factor must be a positive number")
    private BigDecimal conversionFactor;

    @NotNull(message = "isDefault must be specified")
    private Boolean isDefault;

    @NotNull(message = "Unit group ID cannot be null")
    private UUID unitGroupId;
}
