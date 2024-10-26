package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmissionCompartment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmissionCompartmentRepository extends JpaRepository<EmissionCompartment, Long> {
    Optional<EmissionCompartment> findByName(String name);

    List<EmissionCompartment> findByStatus(boolean statusTrue);
  
    Optional<EmissionCompartment> findByIdAndStatus(long emissionCompartmentId, boolean statusTrue);
}
