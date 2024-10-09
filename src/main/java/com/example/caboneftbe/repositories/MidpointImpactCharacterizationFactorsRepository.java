package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MidpointImpactCharacterizationFactorsRepository extends JpaRepository<MidpointImpactCharacterizationFactors,Long> {
}
