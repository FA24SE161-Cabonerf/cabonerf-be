package com.example.caboneftbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SubscriptionTypeDto {
    long id;
    String subscriptionName;
    String description;
    int projectLimit;
    int usageLimit;
    double annualCost;
    double monthlyCost;
    boolean canCreateOrganization;
}
