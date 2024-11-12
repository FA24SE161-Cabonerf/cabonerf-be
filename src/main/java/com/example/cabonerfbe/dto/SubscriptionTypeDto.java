package com.example.cabonerfbe.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SubscriptionTypeDto {
    UUID id;
    String subscriptionName;
    String description;
    int projectLimit;
    int usageLimit;
    BigDecimal annualCost;
    BigDecimal monthlyCost;
    boolean canCreateOrganization;
}
