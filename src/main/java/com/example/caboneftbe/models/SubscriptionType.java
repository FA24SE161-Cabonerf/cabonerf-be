package com.example.caboneftbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
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
