package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class RefreshToken extends Base {
    @Column(length = 500)
    private String token;
    private LocalDateTime createdAt;
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
