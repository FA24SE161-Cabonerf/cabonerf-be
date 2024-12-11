package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface Role repository.
 *
 * @author SonPHH.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findById(UUID id);

    /**
     * Find by name method.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Role> findByName(String name);
}
