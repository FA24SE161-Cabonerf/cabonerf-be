package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class LifeCycleImpactAssessmentMethod extends Base {

    private String name;
    private String description;
    private String version;
    private String reference;

    @ManyToOne
    @JoinColumn(name = "perspective_id")
    private Perspective perspective;
}
