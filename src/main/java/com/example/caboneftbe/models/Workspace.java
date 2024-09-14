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
public class Workspace extends Base{
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Users owner;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
