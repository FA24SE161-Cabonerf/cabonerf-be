package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.UnitDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The class Midpoint impact category response.
 *
 * @author SonPHH.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MidpointImpactCategoryResponse {
    /**
     * The Unit.
     */
    UnitDto unit;
    private UUID id;
    private String name;
    private String description;
    private String abbr;
}
