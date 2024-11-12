package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class SubscriptionType extends Base{

    private String subscriptionName;
    private String description;
    private int projectLimit;
    private int usageLimit;
    private BigDecimal annualCost;
    private BigDecimal monthlyCost;
    private boolean canCreateOrganization;
}
