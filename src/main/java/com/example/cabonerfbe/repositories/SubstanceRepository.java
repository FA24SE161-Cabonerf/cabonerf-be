package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Substance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface Substance repository.
 *
 * @author SonPHH.
 */
@Repository
public interface SubstanceRepository extends JpaRepository<Substance, UUID>, JpaSpecificationExecutor<Substance> {
    /**
     * Find by name method.
     *
     * @param name    the name
     * @param Formula the formula
     * @return the substance
     */
    @Query("SELECT e FROM Substance e WHERE e.name like ?1 AND e.molecularFormula like ?2")
    Substance findByName(String name, String Formula);

    /**
     * Find by name method.
     *
     * @param name the name
     * @return the optional
     */
    @Query("SELECT es FROM Substance es WHERE es.name like :name AND es.status = true")
    Optional<Substance> findByName(@Param("name") String name);

    /**
     * Find by id and status method.
     *
     * @param substanceId the substance id
     * @param statusTrue  the status true
     * @return the optional
     */
    Optional<Substance> findByIdAndStatus(UUID substanceId, boolean statusTrue);
}
