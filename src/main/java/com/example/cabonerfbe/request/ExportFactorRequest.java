package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportFactorRequest {
    @NotNull(message = "Method is required.")
    private UUID methodId;
    @NotNull(message = "Impact category is required.")
    private UUID impactCategoryId;
}
