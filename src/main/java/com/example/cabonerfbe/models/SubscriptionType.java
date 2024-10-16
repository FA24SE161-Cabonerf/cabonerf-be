package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

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
    private double annualCost;
    private double monthlyCost;
    private boolean canCreateOrganization;
}
