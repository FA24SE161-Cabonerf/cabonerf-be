package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Life cycle stage repository.
 *
 * @author SonPHH.
 */
@Repository
public interface LifeCycleStageRepository extends JpaRepository<LifeCycleStage, UUID> {

    /**
     * Find by id and status method.
     *
     * @param id     the id
     * @param status the status
     * @return the optional
     */
    Optional<LifeCycleStage> findByIdAndStatus(UUID id, boolean status);

    @Query("SELECT l FROM LifeCycleStage l WHERE l.status = true ORDER BY l.id ASC")
    List<LifeCycleStage> findAll();
}
