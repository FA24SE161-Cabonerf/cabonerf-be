package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.SubstancesCompartments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubstancesCompartmentsRepository extends JpaRepository<SubstancesCompartments, Long> {
}
