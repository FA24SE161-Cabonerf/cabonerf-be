package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PerspectiveRepository extends JpaRepository<Perspective, UUID> {
    Perspective findByIdAndStatus(UUID id, boolean status);
    List<Perspective> findByStatus(boolean status);
}
