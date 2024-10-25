package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmissionCompartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmissionCompartmentRepository extends JpaRepository<EmissionCompartment, Long> {
    EmissionCompartment findByName(String name);

    List<EmissionCompartment> findByStatus(boolean statusTrue);
}
