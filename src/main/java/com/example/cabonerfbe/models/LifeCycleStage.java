package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class LifeCycleStage extends Base{
    private String name;
    private String description;
    @Lob
    @Column(columnDefinition = "XML")
    private String iconUrl;
}
