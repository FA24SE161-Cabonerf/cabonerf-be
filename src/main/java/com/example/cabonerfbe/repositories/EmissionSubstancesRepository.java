package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Substance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmissionSubstancesRepository extends JpaRepository<Substance, UUID>, JpaSpecificationExecutor<Substance> {
    @Query("SELECT e FROM Substance e WHERE e.name like ?1 AND e.molecularFormula like ?2")
    Substance findByName(String name, String Formula);
    @Query("SELECT es FROM Substance es WHERE es.name like :name AND es.status = true")
    Optional<Substance> findByName(@Param("name") String name);

    Optional<Substance> findByIdAndStatus(UUID emissionSubstanceId, boolean statusTrue);
}
