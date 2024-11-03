package com.example.cabonerfbe.request;

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
public class CreateProcessRequest {
    @NotEmpty(message = "Id is required")
    private UUID id;
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotNull(message = "Life Cycle Stage is required.")
    private UUID lifeCycleStagesId;
    @NotNull(message = "Project is required.")
    private UUID projectId;
}
