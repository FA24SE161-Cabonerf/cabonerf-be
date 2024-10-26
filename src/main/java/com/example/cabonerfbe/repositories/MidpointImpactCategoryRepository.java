package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCategory;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MidpointImpactCategoryRepository extends JpaRepository<MidpointImpactCategory, Long> {
    List<MidpointImpactCategory> findByStatus(boolean statusTrue);

    Optional<MidpointImpactCategory> findByIdAndStatus(long midpointImpactCategoryId, boolean statusTrue);
}
