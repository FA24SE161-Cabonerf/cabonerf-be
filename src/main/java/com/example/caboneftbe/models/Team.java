package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Team extends Base{
    private String name;

    @ManyToOne
    @JoinColumn(name = "role_member_id")
    private RoleMember roleMember;
}
