package com.example.cabonerfbe.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Create factor request.
 *
 * @author SonPHH.
 */
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFactorRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotNull(message = "Chemical Name is required")
    private String chemicalName;
    @NotNull(message = "Molecular formula is required")
    private String molecularFormula;
    @NotNull(message = "Alternative formula is required")
    private String alternativeFormula;
    @NotNull(message = "Cas is required")
    private String cas;
    @NotNull(message = "Value is required.")
    @Min(value = 0, message = "Value must be greater than or equal to zero")
    private BigDecimal value;
    @NotNull(message = "Emission compartment is required.")
    private UUID emissionCompartmentId;
    @NotNull(message = "Method is required.")
    private UUID methodId;
    @NotNull(message = "Impact category is required.")
    private UUID categoryId;
    @NotNull(message = "Unit is required.")
    private UUID unitId;
    private UUID emissionSubstanceId;
}
