package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * The class Create elementary request.
 *
 * @author SonPHH.
 */
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateElementaryRequest {
    @NotNull(message = "Process id is required.")
    private UUID processId;
    @NotNull(message = "Emission substance id is required.")
    private UUID emissionSubstanceId;
    @NotNull(message = "Input is required.")
    private boolean input;
}
