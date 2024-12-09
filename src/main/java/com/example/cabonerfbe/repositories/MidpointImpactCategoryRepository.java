package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MidpointImpactCategoryRepository extends JpaRepository<MidpointImpactCategory, UUID> {
    List<MidpointImpactCategory> findByStatus(boolean statusTrue);

    Optional<MidpointImpactCategory> findByIdAndStatus(UUID midpointImpactCategoryId, boolean statusTrue);
}
