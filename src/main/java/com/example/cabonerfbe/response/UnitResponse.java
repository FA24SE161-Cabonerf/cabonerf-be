package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.UnitGroupDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitResponse {
    private long id;
    private String name;
    private double conversionFactor;
    private Boolean isDefault;
    private UnitGroupDto unitGroup;
}
