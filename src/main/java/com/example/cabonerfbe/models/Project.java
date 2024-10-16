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
}
