package com.example.cabonerfbe.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class Life cycle stage.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class LifeCycleStage extends Base {
    private String name;
    @Column(length = 1000)
    private String description;
    @Column(length = 9000)
    private String iconUrl;

}
