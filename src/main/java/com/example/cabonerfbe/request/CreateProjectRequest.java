package com.example.cabonerfbe.request;

import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
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
public class CreateProjectRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    private String description;
    private String location;
    @NotNull(message = "Method is required")
    private UUID methodId;
    @NotNull(message = "Workspace is required")
    private UUID workspaceId;
}
