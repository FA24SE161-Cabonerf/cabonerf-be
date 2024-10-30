package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.SubstancesCompartments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubstancesCompartmentsRepository extends JpaRepository<SubstancesCompartments, UUID> {

    @Query("SELECT sc FROM SubstancesCompartments sc WHERE sc.status = true")
    List<SubstancesCompartments> findAll();

    @Query("SELECT sc FROM SubstancesCompartments sc WHERE sc.emissionSubstance.id = ?1 AND sc.emissionCompartment.id = ?2")
    Optional<SubstancesCompartments> checkExist(UUID substanceId, UUID compartmentId);

}
