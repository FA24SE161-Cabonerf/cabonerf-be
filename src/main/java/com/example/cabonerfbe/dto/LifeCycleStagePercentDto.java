package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The class Life cycle stage percent dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class LifeCycleStagePercentDto {
    private UUID id;
    private String name;
    private String iconUrl;
    private BigDecimal percent;
}
