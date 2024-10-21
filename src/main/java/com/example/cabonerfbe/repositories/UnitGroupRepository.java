package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UnitGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitGroupRepository extends JpaRepository<UnitGroup, Long> {
    UnitGroup findByIdAndStatus(Long unitGroupId, boolean statusTrue);

    UnitGroup findByNameAndUnitGroupType(String name, String unitGroupType);

    List<UnitGroup> findAllByStatus(boolean statusTrue);
}
