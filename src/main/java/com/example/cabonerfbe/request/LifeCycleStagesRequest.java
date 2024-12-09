package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotEmpty;
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
public class LifeCycleStagesRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotEmpty(message = "Description is required.")
    private String description;
    @NotEmpty(message = "Icon is required.")
    private String iconUrl;
}
