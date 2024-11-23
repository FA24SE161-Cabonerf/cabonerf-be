package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PerspectiveRepository extends JpaRepository<Perspective, UUID> {
    Perspective findByIdAndStatus(UUID id, boolean status);

    List<Perspective> findByStatus(boolean status);

    @Query("SELECT p FROM Perspective p WHERE p.name like ?1 AND p.status = true AND p.id <> ?2")
    Perspective findByNameAAndId(String name, UUID id);
}
