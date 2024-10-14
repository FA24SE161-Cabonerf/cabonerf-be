package com.example.caboneftbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
