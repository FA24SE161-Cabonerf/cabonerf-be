package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCharacterizationFactors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MidpointImpactCharacterizationFactorsRepository extends JpaRepository<MidpointImpactCharacterizationFactors, UUID> {

//    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f WHERE f.impactMethodCategory.id = ?1 AND f.emissionSubstances.name like ?2 AND f.emissionCompartment.id = ?3 AND f.emissionSubstances.molecularFormula like ?4")
//    MidpointImpactCharacterizationFactors findByImpactMethodCategoryIdAndEmissionSubstancesName(long methodId, String name, long emissionCompartmentId, String molecular);
//
//    @Query("SELECT f FROM MidpointImpactCharacterizationFactors f WHERE f.emissionSubstances.id = ?1")
//    List<MidpointImpactCharacterizationFactors> findByEmissionSubstancesId(long emissionSubstancesId);
}
