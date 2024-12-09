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
public class LifeCycleImpactAssessmentMethod extends Base {

    private String name;
    private String description;
    private String version;
    private String reference;

    @ManyToOne
    @JoinColumn(name = "perspective_id")
    private Perspective perspective;
}
