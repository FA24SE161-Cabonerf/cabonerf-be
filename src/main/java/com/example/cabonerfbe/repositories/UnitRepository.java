package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query("SELECT a FROM Unit a WHERE a.name LIKE CONCAT('%', ?1, '%')")
    List<Unit> findByName(String name);

    @Query("SELECT u FROM Unit u WHERE u.name like ?1 ")
    Unit findByNameUnit(String name);

    @Query("SELECT u FROM Unit u WHERE u.unitGroup.id = ?1")
    List<Unit> findAllByUnitGroupId(long unitGroupId);
    @Query("SELECT u FROM Unit u WHERE u.unitGroup.id = ?1")
    Page<Unit> findAllByUnitGroupIdWithPage(long unitGroupId, Pageable pageable);
}
