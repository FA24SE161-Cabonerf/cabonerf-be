package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * The class Users.
 *
 * @author SonPHH.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Getter
@Setter
@SuperBuilder
public class Users extends Base implements UserDetails {

    private String fullName;
    private String email;
    private String password;
    private String phone;
    @Column(length = 9000)
    private String profilePictureUrl;
    private String bio;

    @ManyToOne
    @JoinColumn(name = "user_verify_status_id")
    private UserVerifyStatus userVerifyStatus;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

