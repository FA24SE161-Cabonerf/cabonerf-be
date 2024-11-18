package com.example.cabonerfbe.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContributionBreakdown {
    private String processName;
    private double co2Equivalent; // Contribution value
    private double percentageOfTotal; // Percentage relative to total
    private List<ContributionBreakdown> subProcesses; // Hierarchical children
}
