package com.example.cabonerfbe.request;

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
public class GetAllProcessRequest {
    @NotNull(message = "Project id is required.")
    private long projectId;
    @NotNull(message = "Life Cycle Impact Assessment Method id is required.")
    private long methodId;
}
