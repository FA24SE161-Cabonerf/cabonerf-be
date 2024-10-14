package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.Unit;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MidpointImpactCategoryDto {
    private long id;
    private String name;
    private String description;
    private String abbr;
    private UnitDto unit;
}