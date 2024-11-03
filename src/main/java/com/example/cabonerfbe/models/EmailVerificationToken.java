package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class EmailVerificationToken extends Base{
    @Column(length = 500)
    private String token;
    private Timestamp expiryDate;
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

}
