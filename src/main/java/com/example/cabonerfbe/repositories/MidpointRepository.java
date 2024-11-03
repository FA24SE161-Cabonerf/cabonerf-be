package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.enums.QueryStrings;
import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MidpointRepository extends JpaRepository<MidpointImpactCharacterizationFactors, UUID> {
    List<MidpointImpactCharacterizationFactors> findAllByStatus(boolean status);

    @Query(value = QueryStrings.FIND_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVES,
            countQuery = QueryStrings.COUNT_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVE,
            nativeQuery = true)
    Page<Object[]> findAllWithPerspective(Pageable pageable);

    Optional<MidpointImpactCharacterizationFactors> findByIdAndStatus(UUID id, boolean status);
}
