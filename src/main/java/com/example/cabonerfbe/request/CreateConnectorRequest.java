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
public class CreateConnectorRequest {
    @NotNull(message = "Start process id is required.")
    private UUID startProcessId;
    @NotNull(message = "End process id is required.")
    private UUID endProcessId;
    private UUID startExchangesId;
    private UUID endExchangesId;
}
