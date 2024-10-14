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
public class CreateConnectorRequest {
    @NotNull(message = "Start process id is required.")
    private long startProcessId;
    @NotNull(message = "End process id is required.")
    private long endProcessId;
    @NotNull(message = "Start exchange id is required.")
    private long startExchangesId;
    private Long endExchangesId;
}
