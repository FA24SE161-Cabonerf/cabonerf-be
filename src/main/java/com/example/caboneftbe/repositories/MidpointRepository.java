package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MidpointRepository extends JpaRepository<MidpointImpactCharacterizationFactors, Long> {
}
