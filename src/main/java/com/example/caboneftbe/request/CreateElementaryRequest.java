package com.example.caboneftbe.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateElementaryRequest {
    @NotNull(message = "Process id is required.")
    private long processId;
    @NotNull(message = "Emission Substance id is required.")
    private long emissionSubstanceId;
    @NotNull(message = "Input is required.")
    private boolean input;
}
