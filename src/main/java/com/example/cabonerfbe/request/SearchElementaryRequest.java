package com.example.cabonerfbe.request;

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
public class SearchElementaryRequest {
    String search;
    UUID emissionCompartmentId;
    @NotNull(message = "Method is required")
    UUID methodId;
    UUID impactCategoryId;
}
