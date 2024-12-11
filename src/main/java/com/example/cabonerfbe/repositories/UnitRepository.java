package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Unit;
import com.example.cabonerfbe.models.UnitGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Unit repository.
 *
 * @author SonPHH.
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, UUID> {
    /**
     * Find by name method.
     *
     * @param name the name
     * @return the list
     */
    @Query("SELECT a FROM Unit a WHERE a.name LIKE CONCAT('%', ?1, '%')")
    List<Unit> findByName(String name);

    /**
     * Find by name unit method.
     *
     * @param name the name
     * @return the unit
     */
    @Query("SELECT u FROM Unit u WHERE u.name like ?1 ")
    Unit findByNameUnit(String name);

    /**
     * Find by unit group method.
     *
     * @param name the name
     * @return the unit
     */
    @Query("SELECT u FROM Unit u WHERE u.name like ?1 AND u.unitGroup.unitGroupType like 'Normal'")
    Unit findByUnitGroup(String name);

    /**
     * Find all by status method.
     *
     * @param statusTrue the status true
     * @return the list
     */
    List<Unit> findAllByStatus(boolean statusTrue);

    /**
     * Find all by status and unit group id method.
     *
     * @param status      the status
     * @param unitGroupId the unit group id
     * @return the list
     */
    List<Unit> findAllByStatusAndUnitGroupId(boolean status, UUID unitGroupId);

    /**
     * Find by id and status method.
     *
     * @param unitId     the unit id
     * @param statusTrue the status true
     * @return the optional
     */
    Optional<Unit> findByIdAndStatus(UUID unitId, boolean statusTrue);

    /**
     * Find all by unit group id method.
     *
     * @param unitGroupId the unit group id
     * @return the list
     */
    @Query("SELECT u FROM Unit u WHERE u.unitGroup.id = ?1")
    List<Unit> findAllByUnitGroupId(UUID unitGroupId);

    /**
     * Find all by unit group id with page method.
     *
     * @param unitGroupId the unit group id
     * @param pageable    the pageable
     * @return the page
     */
    @Query("SELECT u FROM Unit u WHERE u.unitGroup.id = ?1")
    Page<Unit> findAllByUnitGroupIdWithPage(UUID unitGroupId, Pageable pageable);

    /**
     * Exists by is default and status and unit group method.
     *
     * @param isDefault the is default
     * @param status    the status
     * @param unitGroup the unit group
     * @return the boolean
     */
    boolean existsByIsDefaultAndStatusAndUnitGroup(Boolean isDefault, boolean status, UnitGroup unitGroup);

    /**
     * Find default method.
     *
     * @param unitGroupId the unit group id
     * @return the unit
     */
    @Query("SELECT u FROM Unit u JOIN FETCH u.unitGroup ug WHERE ug.id = :unitGroupId AND u.isDefault = true AND u.status = true AND ug.status = true")
    Unit findDefault(@Param("unitGroupId") UUID unitGroupId);


}
