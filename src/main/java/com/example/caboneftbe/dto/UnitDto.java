package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.UnitGroup;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitDto {
    private long id;
    private String name;
    private double conversionFactor;

    private UnitGroupDto unitGroup;

    private boolean isDefault;
}
