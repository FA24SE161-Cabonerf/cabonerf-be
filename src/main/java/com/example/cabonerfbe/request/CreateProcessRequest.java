package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotEmpty;
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
public class CreateProcessRequest {

    @NotEmpty(message = "Name is required.")
    private String name;
    private String description;
    @NotNull(message = "Life Cycle Stage is required.")
    private long lifeCycleStageId;
    @NotEmpty(message = "Project is required.")
    private long projectId;
}
