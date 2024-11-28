package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmissionCompartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmissionCompartmentRepository extends JpaRepository<EmissionCompartment, UUID> {
    @Query("SELECT ec FROM EmissionCompartment ec WHERE UPPER(ec.name) like UPPER(:name) AND ec.status = true")
    Optional<EmissionCompartment> findByName(@Param("name") String name);

    List<EmissionCompartment> findByStatus(boolean statusTrue);

    Optional<EmissionCompartment> findByIdAndStatus(UUID emissionCompartmentId, boolean statusTrue);

    @Query("SELECT ec FROM EmissionCompartment ec WHERE ec.status = true")
    List<EmissionCompartment> findAllByStatus();
}
