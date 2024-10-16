package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MidpointRepository extends JpaRepository<MidpointImpactCharacterizationFactors, Long> {
    List<MidpointImpactCharacterizationFactors> findAllByStatus(boolean status);

    @Query(value = "SELECT es.id, micf.cas as cas_number, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name as compartment_name, " +
            "MAX(CASE WHEN p.name = 'Individualist' THEN micf.decimal_value ELSE NULL END) AS individualist, " +
            "MAX(CASE WHEN p.name = 'Hierarchist' THEN micf.decimal_value ELSE NULL END) AS hierarchist, " +
            "MAX(CASE WHEN p.name = 'Egalitarian' THEN micf.decimal_value ELSE NULL END) AS egalitarian " +
            "FROM emission_substances es " +
            "JOIN midpoint_impact_characterization_factors micf ON micf.emission_substances_id = es.id " +
            "JOIN impact_method_category imc ON imc.id = micf.impact_method_category_id " +
            "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id " +
            "JOIN perspective p ON p.id = lciam.perspective_id " +
            "JOIN emission_compartment ec ON ec.id = micf.emission_compartment_id " +
            "GROUP BY es.id, micf.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name " +
            "ORDER BY es.id ASC", nativeQuery = true)
    List<Object[]> findAllWithPerspective(Pageable pageable);
}
