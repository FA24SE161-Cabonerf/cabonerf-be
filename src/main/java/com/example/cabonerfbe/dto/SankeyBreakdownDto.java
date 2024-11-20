package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SankeyBreakdownDto {
    private List<SankeyNode> nodes;
    private List<SankeyLink> links;

}

