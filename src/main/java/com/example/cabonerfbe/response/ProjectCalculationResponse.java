package com.example.cabonerfbe.response;

import com.example.cabonerfbe.dto.ConnectorPercentDto;
import com.example.cabonerfbe.dto.SankeyBreakdownDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectCalculationResponse {
    List<ConnectorPercentDto> connectorPercent;
    SankeyBreakdownDto sankeyBreakdown;
}
