package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Perspective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerspectiveRepository extends JpaRepository<Perspective, Long> {
    Perspective findByIdAndStatus(Long id, boolean status);
    List<Perspective> findByStatus(boolean status);
}
