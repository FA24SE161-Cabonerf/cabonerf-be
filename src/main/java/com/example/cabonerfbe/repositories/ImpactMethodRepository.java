package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Impact method repository.
 *
 * @author SonPHH.
 */
public interface ImpactMethodRepository extends JpaRepository<LifeCycleImpactAssessmentMethod, UUID> {

    /**
     * Exists by name ignore case and version ignore case and perspective id method.
     *
     * @param name          the name
     * @param version       the version
     * @param perspectiveId the perspective id
     * @return the boolean
     */
    @Query("""
             select (count(l) > 0) from LifeCycleImpactAssessmentMethod l
             where upper(l.name) = upper(?1) and upper(l.version) = upper(?2) and l.perspective.id = ?3 and l.status = true
            """)
    boolean existsByNameIgnoreCaseAndVersionIgnoreCaseAndPerspectiveId(String name, String version, UUID perspectiveId);

    /**
     * Find by id and status method.
     *
     * @param methodId the method id
     * @param status   the status
     * @return the optional
     */
    Optional<LifeCycleImpactAssessmentMethod> findByIdAndStatus(UUID methodId, boolean status);

    /**
     * Find by status method.
     *
     * @param statusTrue the status true
     * @return the list
     */
    List<LifeCycleImpactAssessmentMethod> findByStatus(boolean statusTrue);
}
