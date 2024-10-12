package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MidpointImpactCharacterizationFactorsRepository extends JpaRepository<MidpointImpactCharacterizationFactors,Long> {

    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f WHERE f.impactMethodCategory.id = ?1 AND f.emissionSubstances.name like ?2 AND f.emissionCompartment.id = ?3 AND f.emissionSubstances.molecularFormula like ?4")
    MidpointImpactCharacterizationFactors findByImpactMethodCategoryIdAndEmissionSubstancesName(long methodId, String name, long emissionCompartmentId, String molecular);
}
