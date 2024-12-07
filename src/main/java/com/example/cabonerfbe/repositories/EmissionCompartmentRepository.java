package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmissionCompartment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmissionCompartmentRepository extends JpaRepository<EmissionCompartment, UUID> {
    Optional<EmissionCompartment> findByName(String name);

    List<EmissionCompartment> findByStatus(boolean statusTrue);
  
    Optional<EmissionCompartment> findByIdAndStatus(UUID emissionCompartmentId, boolean statusTrue);
}
