package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UnitGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Unit group repository.
 *
 * @author SonPHH.
 */
@Repository
public interface UnitGroupRepository extends JpaRepository<UnitGroup, UUID> {
    /**
     * Find by id and status method.
     *
     * @param unitGroupId the unit group id
     * @param statusTrue  the status true
     * @return the optional
     */
    Optional<UnitGroup> findByIdAndStatus(UUID unitGroupId, boolean statusTrue);

    /**
     * Find by name and unit group type and status method.
     *
     * @param name          the name
     * @param unitGroupType the unit group type
     * @param status        the status
     * @return the unit group
     */
    UnitGroup findByNameAndUnitGroupTypeAndStatus(String name, String unitGroupType, boolean status);

    /**
     * Find all by status method.
     *
     * @param statusTrue the status true
     * @return the list
     */
    List<UnitGroup> findAllByStatus(boolean statusTrue);

    /**
     * Check exist method.
     *
     * @param id   the id
     * @param name the name
     * @return the optional
     */
    @Query("SELECT u FROM UnitGroup u WHERE u.name like ?2 AND u.status = true AND u.id <> ?1")
    Optional<UnitGroup> checkExist(UUID id, String name);
}
