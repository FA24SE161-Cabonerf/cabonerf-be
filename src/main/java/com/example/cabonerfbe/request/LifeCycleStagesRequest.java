package com.example.cabonerfbe.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

/**
 * The class Life cycle stages request.
 *
 * @author SonPHH.
 */
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifeCycleStagesRequest {
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotEmpty(message = "Description is required.")
    @Length(max = 1000)
    private String description;
    @NotEmpty(message = "Icon is required.")
    private String iconUrl;
}
