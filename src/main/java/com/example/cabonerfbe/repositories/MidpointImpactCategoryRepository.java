package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.MidpointImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Midpoint impact category repository.
 *
 * @author SonPHH.
 */
public interface MidpointImpactCategoryRepository extends JpaRepository<MidpointImpactCategory, UUID> {
    /**
     * Find by status method.
     *
     * @param statusTrue the status true
     * @return the list
     */
    List<MidpointImpactCategory> findByStatus(boolean statusTrue);

    /**
     * Find by id and status method.
     *
     * @param midpointImpactCategoryId the midpoint impact category id
     * @param statusTrue               the status true
     * @return the optional
     */
    Optional<MidpointImpactCategory> findByIdAndStatus(UUID midpointImpactCategoryId, boolean statusTrue);
}
