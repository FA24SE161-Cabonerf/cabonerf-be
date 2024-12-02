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
public class Project extends Base {

    private String name;
    private String description;
    private String location;
    private Boolean favorite;

    @ManyToOne
    @JoinColumn(name = "method_id")
    private LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "industry_code_id")
    private IndustryCode industryCode;

}
