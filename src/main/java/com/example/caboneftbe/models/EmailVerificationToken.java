package com.example.caboneftbe.models;

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
    private String token;
    private Timestamp expiryDate;
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

}
