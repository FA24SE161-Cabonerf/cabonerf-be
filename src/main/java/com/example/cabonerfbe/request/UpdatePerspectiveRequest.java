package com.example.cabonerfbe.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

/**
 * The class Update perspective request.
 *
 * @author SonPHH.
 */
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePerspectiveRequest {
    private String name;
    @Length(max = 1000)
    private String description;
    private String abbr;
}
