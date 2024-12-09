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
public class AddObjectLibraryRequest {
    @NotEmpty(message = "UserId is required.")
    private UUID userId;
    @NotNull(message = "ObjectLibraryId is required.")
    private UUID objectLibId;
    @NotNull(message = "ProjectId is required.")
    private UUID projectId;
}
