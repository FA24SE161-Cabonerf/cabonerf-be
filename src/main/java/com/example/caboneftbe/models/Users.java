package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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
    private String profilePictureUrl;
    private String bio;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private SubscriptionType subscription;

    @ManyToOne
    @JoinColumn(name = "user_verify_status_id")
    private UserVerifyStatus userVerifyStatus;

    @ManyToOne
    @JoinColumn(name = "user_status_id")
    private UserStatus userStatus;

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

