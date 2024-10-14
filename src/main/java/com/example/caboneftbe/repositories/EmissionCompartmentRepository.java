package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.EmissionCompartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmissionCompartmentRepository extends JpaRepository<EmissionCompartment, Long> {
    EmissionCompartment findByName(String name);
}
