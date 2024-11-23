package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.UnitDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MidpointImpactCategoryResponse {
    UnitDto unit;
    private UUID id;
    private String name;
    private String description;
    private String abbr;
}
