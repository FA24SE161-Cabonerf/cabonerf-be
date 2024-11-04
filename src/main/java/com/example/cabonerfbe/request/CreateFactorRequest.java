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

import java.util.UUID;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFactorRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    private String chemicalName;
    private String molecularFormula;
    private String alternativeFormula;
    private String cas;
    @NotNull(message = "Value is required.")
    @Min(value = 0,message = "Value must be greater than or equal to zero")
    private double value;
    @NotNull(message = "Emission compartment is required.")
    private UUID emissionCompartmentId;
    @NotNull(message = "Method is required.")
    private UUID methodId;
    @NotNull(message = "Impact category is required.")
    private UUID categoryId;
    @NotNull(message = "Unit group is required.")
    private UUID unitGroupId;
}
