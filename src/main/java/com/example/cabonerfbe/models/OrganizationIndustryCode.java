package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class OrganizationIndustryCode extends Base{

    @ManyToOne
    @JoinColumn(name = "industry_code_id")
    private IndustryCode industryCode;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
