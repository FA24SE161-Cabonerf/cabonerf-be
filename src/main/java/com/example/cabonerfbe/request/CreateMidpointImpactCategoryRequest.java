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
 * The class Create midpoint impact category request.
 *
 * @author SonPHH.
 */
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMidpointImpactCategoryRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    @Length(max = 1000)
    private String description;
    @NotEmpty(message = "Abbr is required.")
    private String abbr;
    @NotNull(message = "Unit is required.")
    private UUID unitId;
}
