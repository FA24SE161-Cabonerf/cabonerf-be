package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LifeCycleStageRepository extends JpaRepository<LifeCycleStage, UUID> {

    Optional<LifeCycleStage> findByIdAndStatus(UUID id, boolean status);
    @Query("SELECT l FROM LifeCycleStage l WHERE l.status = true ORDER BY l.id ASC")
    List<LifeCycleStage> findAll();
}
