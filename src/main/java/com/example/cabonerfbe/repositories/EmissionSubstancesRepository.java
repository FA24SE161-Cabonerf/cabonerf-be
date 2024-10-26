package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmissionSubstances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmissionSubstancesRepository extends JpaRepository<EmissionSubstances,Long> {
    @Query("SELECT e FROM EmissionSubstances e WHERE e.name like ?1 AND e.molecularFormula like ?2")
    EmissionSubstances findByName(String name, String Formula);
    Optional<EmissionSubstances> findByName(String name);
}
