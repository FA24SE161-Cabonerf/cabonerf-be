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

@Repository
public interface UnitRepository extends JpaRepository<Unit, UUID> {
    @Query("SELECT a FROM Unit a WHERE a.name LIKE CONCAT('%', ?1, '%')")
    List<Unit> findByName(String name);

    @Query("SELECT u FROM Unit u WHERE u.name like ?1 ")
    Unit findByNameUnit(String name);

    @Query("SELECT u FROM Unit u WHERE u.name like ?1 AND u.unitGroup.unitGroupType like 'Normal'")
    Unit findByUnitGroup(String name);

    List<Unit> findAllByStatus(boolean statusTrue);

    List<Unit> findAllByStatusAndUnitGroupId(boolean status, UUID unitGroupId);

    Optional<Unit> findByIdAndStatus(UUID unitId, boolean statusTrue);

    @Query("SELECT u FROM Unit u WHERE u.unitGroup.id = ?1")
    List<Unit> findAllByUnitGroupId(UUID unitGroupId);

    @Query("SELECT u FROM Unit u WHERE u.unitGroup.id = ?1")
    Page<Unit> findAllByUnitGroupIdWithPage(UUID unitGroupId, Pageable pageable);

    boolean existsByIsDefaultAndStatusAndUnitGroup(Boolean isDefault, boolean status, UnitGroup unitGroup);

    @Query("SELECT u FROM Unit u JOIN FETCH u.unitGroup ug WHERE ug.id = :unitGroupId AND u.isDefault = true AND u.status = true AND ug.status = true")
    Unit findDefault(@Param("unitGroupId") UUID unitGroupId);


}
