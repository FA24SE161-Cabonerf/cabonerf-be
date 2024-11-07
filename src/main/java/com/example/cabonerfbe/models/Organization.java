package com.example.cabonerfbe.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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
public class Organization extends Base{
    private String name;
    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL)
    private Contract contract;
}
