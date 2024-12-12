package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class Life cycle impact assessment method.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class LifeCycleImpactAssessmentMethod extends Base {

    private String name;
    @Column(length = 1000)
    private String description;
    private String version;
    private String reference;

    @ManyToOne
    @JoinColumn(name = "perspective_id")
    private Perspective perspective;
}
