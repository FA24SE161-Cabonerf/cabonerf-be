package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MidpointRepository extends JpaRepository<MidpointImpactCharacterizationFactors, Long> {
}
