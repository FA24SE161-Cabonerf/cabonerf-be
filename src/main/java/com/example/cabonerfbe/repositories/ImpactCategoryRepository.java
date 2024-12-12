package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.enums.QueryStrings;
import com.example.cabonerfbe.models.ImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Impact category repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ImpactCategoryRepository extends JpaRepository<ImpactCategory, UUID> {
    /**
     * Find by name method.
     *
     * @param name the name
     * @return the impact category
     */
    @Query("SELECT a FROM ImpactCategory a WHERE UPPER(a.name) LIKE CONCAT('%', ?1, '%')")
    ImpactCategory findByName(String name);

    /**
     * Find all by status method.
     *
     * @param status the status
     * @return the list
     */
    List<ImpactCategory> findAllByStatus(boolean status);

    /**
     * Find all by impact method id method.
     *
     * @param methodId the method id
     * @return the list
     */
    @Query(QueryStrings.FIND_CATEGORY_BY_IMPACT_METHOD_ID)
    List<ImpactCategory> findAllByImpactMethodId(@Param("methodId") UUID methodId);

    /**
     * Find by id and status method.
     *
     * @param categoryId the category id
     * @param statusTrue the status true
     * @return the optional
     */
    Optional<ImpactCategory> findByIdAndStatus(UUID categoryId, boolean statusTrue);


}
