package com.example.cabonerfbe.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MidpointSubstanceFactorsResponse {
    private UUID id;
    private String casNumber;
    private String name;
    private String chemicalName;
    private String compartmentName;
    private String molecularFormula;
    private String alternativeFormula;
    private BigDecimal individualist;
    private BigDecimal hierarchist;
    private BigDecimal egalitarian;

    public void setMethodValue(String methodName, BigDecimal value) {
        switch (methodName) {
            case "Individualist":
                this.individualist = value;
                break;
            case "Hierarchist":
                this.hierarchist = value;
                break;
            case "Egalitarian":
                this.egalitarian = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid method name: " + methodName);
        }
    }
}
