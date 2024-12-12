package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.EmissionCompartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Emission compartment repository.
 *
 * @author SonPHH.
 */
@Repository
public interface EmissionCompartmentRepository extends JpaRepository<EmissionCompartment, UUID> {
    /**
     * Find by name method.
     *
     * @param name the name
     * @return the optional
     */
    @Query("SELECT ec FROM EmissionCompartment ec WHERE UPPER(ec.name) like UPPER(:name) AND ec.status = true")
    Optional<EmissionCompartment> findByName(@Param("name") String name);

    /**
     * Find by status method.
     *
     * @param statusTrue the status true
     * @return the list
     */
    List<EmissionCompartment> findByStatus(boolean statusTrue);

    /**
     * Find by id and status method.
     *
     * @param emissionCompartmentId the emission compartment id
     * @param statusTrue            the status true
     * @return the optional
     */
    Optional<EmissionCompartment> findByIdAndStatus(UUID emissionCompartmentId, boolean statusTrue);

    /**
     * Find all by status method.
     *
     * @return the list
     */
    @Query("SELECT ec FROM EmissionCompartment ec WHERE ec.status = true")
    List<EmissionCompartment> findAllByStatus();
}
