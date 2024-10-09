package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.EmissionSubstances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmissionSubstancesRepository extends JpaRepository<EmissionSubstances,Long> {
    EmissionSubstances findByName(String name);
}
