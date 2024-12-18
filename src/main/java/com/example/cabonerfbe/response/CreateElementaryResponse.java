package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ExchangesDto;
import com.example.cabonerfbe.dto.LifeCycleStageDto;
import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The class Create elementary response.
 *
 * @author SonPHH.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateElementaryResponse {
    private UUID id;
    private String name;
    private String description;
    private LifeCycleStageDto lifeCycleStage;
    private UUID projectId;
    private BigDecimal overallProductFlowRequired;
    private List<ProcessImpactValueDto> impacts;
    private List<ExchangesDto> exchanges;
}
