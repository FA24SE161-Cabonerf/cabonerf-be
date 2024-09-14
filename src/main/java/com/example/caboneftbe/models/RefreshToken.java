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
public class RefreshToken extends Base{
    private String token;
    private LocalDateTime createdAt;
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
