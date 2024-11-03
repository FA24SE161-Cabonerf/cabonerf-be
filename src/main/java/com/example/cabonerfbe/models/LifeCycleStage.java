package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class LifeCycleStage extends Base{
    private String name;
    private String description;
    @Column(length = 9000)
    private String iconUrl;

}
