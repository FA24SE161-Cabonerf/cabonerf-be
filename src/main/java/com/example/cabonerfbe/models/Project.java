package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Project extends Base{

    private String name;
    private String description;
    private String location;

    @ManyToOne
    @JoinColumn(name = "method_id")
    private LifeCycleImpactAssessmentMethod lifeCycleImpactAssessmentMethod;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

}
