package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.EmissionSubstances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmissionSubstancesRepository extends JpaRepository<EmissionSubstances,Long> {
    @Query("SELECT e FROM EmissionSubstances e WHERE e.name like ?1 AND e.molecularFormula like ?2")
    EmissionSubstances findByName(String name, String Formula);
}
