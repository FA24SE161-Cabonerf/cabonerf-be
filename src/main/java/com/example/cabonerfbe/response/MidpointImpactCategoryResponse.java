package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.UnitDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MidpointImpactCategoryResponse {
    private long id;
    private String name;
    private String description;
    private String abbr;

    UnitDto unit;
}
