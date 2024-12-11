package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * The class Email verification token.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class EmailVerificationToken extends Base {
    @Column(length = 500)
    private String token;
    private Timestamp expiryDate;
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

}
