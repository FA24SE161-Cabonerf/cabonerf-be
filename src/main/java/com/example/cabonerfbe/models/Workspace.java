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
public class Workspace extends Base {
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Users owner;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
