package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table
public class Team extends Base{
    private String name;

    @ManyToOne
    @JoinColumn(name = "role_member_id")
    private RoleMember roleMember;
}
