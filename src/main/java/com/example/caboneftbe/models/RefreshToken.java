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
public class RefreshToken extends Base{
    private String token;
    private LocalDateTime createdAt;
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
