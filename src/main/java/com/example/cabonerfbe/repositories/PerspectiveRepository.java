package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * The interface Perspective repository.
 *
 * @author SonPHH.
 */
public interface PerspectiveRepository extends JpaRepository<Perspective, UUID> {
    /**
     * Find by id and status method.
     *
     * @param id     the id
     * @param status the status
     * @return the perspective
     */
    Perspective findByIdAndStatus(UUID id, boolean status);

    /**
     * Find by status method.
     *
     * @param status the status
     * @return the list
     */
    List<Perspective> findByStatus(boolean status);

    /**
     * Find by name a and id method.
     *
     * @param name the name
     * @param id   the id
     * @return the perspective
     */
    @Query("SELECT p FROM Perspective p WHERE p.name like ?1 AND p.status = true AND p.id <> ?2")
    Perspective findByNameAAndId(String name, UUID id);
}
