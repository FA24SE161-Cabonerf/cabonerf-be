package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.CarbonIntensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The interface Carbon intensity repository.
 *
 * @author SonPHH.
 */
@Repository
public interface CarbonIntensityRepository extends JpaRepository<CarbonIntensity, UUID> {
}
