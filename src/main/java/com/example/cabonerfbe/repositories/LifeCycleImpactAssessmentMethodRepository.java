package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleImpactAssessmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Life cycle impact assessment method repository.
 *
 * @author SonPHH.
 */
@Repository
public interface LifeCycleImpactAssessmentMethodRepository extends JpaRepository<LifeCycleImpactAssessmentMethod, UUID> {

    /**
     * Find by name method.
     *
     * @param perspectiveName                     the perspective name
     * @param lifeCycleImpactAssessmentMethodName the life cycle impact assessment method name
     * @return the life cycle impact assessment method
     */
    @Query("SELECT a FROM LifeCycleImpactAssessmentMethod a " +
            "JOIN FETCH a.perspective p " +
            "WHERE p.name LIKE ?1 AND a.name LIKE ?2")
    LifeCycleImpactAssessmentMethod findByName(String perspectiveName, String lifeCycleImpactAssessmentMethodName);


    /**
     * Find all by name method.
     *
     * @param name the name
     * @return the list
     */
    @Query("SELECT a FROM LifeCycleImpactAssessmentMethod a WHERE a.name like ?1 ")
    List<LifeCycleImpactAssessmentMethod> findAllByName(String name);

    /**
     * Find by id and status method.
     *
     * @param id     the id
     * @param status the status
     * @return the optional
     */
    Optional<LifeCycleImpactAssessmentMethod> findByIdAndStatus(UUID id, boolean status);

    /**
     * Gets all with name.
     *
     * @return the all with name
     */
    @Query("SELECT m.name FROM LifeCycleImpactAssessmentMethod m WHERE m.status = true GROUP BY m.name")
    List<String> getAllWithName();


}
