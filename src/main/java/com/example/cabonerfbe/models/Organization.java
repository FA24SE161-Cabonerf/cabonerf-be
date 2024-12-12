package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class Organization.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Organization extends Base {
    private String name;
    @Column(length = 1000)
    private String description;
    private String taxCode;
    @Column(length = 9000)
    private String logo;
    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL)
    private Contract contract;
}
