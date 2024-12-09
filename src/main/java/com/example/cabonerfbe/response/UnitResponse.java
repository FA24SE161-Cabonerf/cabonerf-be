package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.UnitGroupDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class UnitResponse {
    private UUID id;
    private String name;
    private BigDecimal conversionFactor;
    private Boolean isDefault;
    private UnitGroupDto unitGroup;
}
