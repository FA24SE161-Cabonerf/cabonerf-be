package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The class Project detail response dto.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectDetailResponseDto {
    private String projectName;
    private LocalDateTime modifiedAt;
    private String assessmentMethod;
    private String impactCategory;
    private String midpointCharacterizationFactor;
    private String midpointAbbr;
    private BigDecimal projectValue;
    private String unit;

}
