package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

/**
 * The class Update process request.
 *
 * @author SonPHH.
 */
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProcessRequest {
    @NotEmpty(message = "Name is required")
    private String name;
    @NotNull(message = "Description is required")
    @Length(max = 1000)
    private String description;
    @NotNull(message = "Life Cycle Stage is required")
    private UUID lifeCycleStagesId;
}
