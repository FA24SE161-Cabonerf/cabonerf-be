package com.example.caboneftbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class UserVerifyStatus extends Base {

    private String statusName;
    private String description;
}
