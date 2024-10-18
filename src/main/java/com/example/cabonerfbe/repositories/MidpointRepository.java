package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.enums.QueryStrings;
import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MidpointRepository extends JpaRepository<MidpointImpactCharacterizationFactors, Long> {
    List<MidpointImpactCharacterizationFactors> findAllByStatus(boolean status);

    @Query(value = QueryStrings.FIND_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVES,
            countQuery = "SELECT COUNT(*) FROM emission_substances es " +
                    "JOIN midpoint_impact_characterization_factors micf ON micf.emission_substances_id = es.id " +
                    "JOIN impact_method_category imc ON imc.id = micf.impact_method_category_id " +
                    "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id " +
                    "JOIN perspective p ON p.id = lciam.perspective_id " +
                    "JOIN emission_compartment ec ON ec.id = micf.emission_compartment_id",
            nativeQuery = true)
    Page<Object[]> findAllWithPerspective(Pageable pageable);
}
