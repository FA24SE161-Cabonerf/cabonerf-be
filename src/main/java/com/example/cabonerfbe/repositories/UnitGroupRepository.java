package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UnitGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UnitGroupRepository extends JpaRepository<UnitGroup, UUID> {
    UnitGroup findByIdAndStatus(UUID unitGroupId, boolean statusTrue);

    UnitGroup findByNameAndUnitGroupType(String name, String unitGroupType);

    List<UnitGroup> findAllByStatus(boolean statusTrue);
}
