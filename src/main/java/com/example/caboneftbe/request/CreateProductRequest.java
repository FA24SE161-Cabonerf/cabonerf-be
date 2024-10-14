package com.example.caboneftbe.request;

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
public class CreateProductRequest {
    @NotNull(message = "Process id is required.")
    private long processId;
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotNull(message = "Input is required.")
    private boolean input;
}
