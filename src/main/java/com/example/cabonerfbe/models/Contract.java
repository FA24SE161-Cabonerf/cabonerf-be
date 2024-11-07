package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
public class Contract extends Base{
    private String url;
    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;
}
