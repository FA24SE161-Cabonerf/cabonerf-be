package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MidpointImpactCategoryRepository extends JpaRepository<MidpointImpactCategory, Long> {
    List<MidpointImpactCategory> findByStatus(boolean statusTrue);
}
