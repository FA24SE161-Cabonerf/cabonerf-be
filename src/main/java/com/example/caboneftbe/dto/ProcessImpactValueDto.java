package com.example.caboneftbe.dto;

import com.example.caboneftbe.models.ImpactMethodCategory;
import com.example.caboneftbe.models.Process;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProcessImpactValueDto {
    private long id;
    private ImpactMethodCategoryDto impactMethodCategory;
    private double unitLevel;
    private double systemLevel;
    private double overallImpactContribution;
    private double previousProcessValue;
}
