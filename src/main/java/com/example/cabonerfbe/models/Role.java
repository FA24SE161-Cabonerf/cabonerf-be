package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Role extends Base implements GrantedAuthority {

    private String name;
    @Override
    public String getAuthority() {
        return name;
    }
}
