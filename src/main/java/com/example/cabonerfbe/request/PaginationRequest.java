package com.example.cabonerfbe.request;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The class Pagination request.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationRequest {
    @Min(value = 1, message = "Current page cannot be less than 1")
    int currentPage = 1;
    @Min(value = 1, message = "Page size cannot be less than 1")
    int pageSize = 20;
}
