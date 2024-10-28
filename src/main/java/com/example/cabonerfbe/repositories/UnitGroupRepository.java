package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.UnitGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitGroupRepository extends JpaRepository<UnitGroup, UUID> {
    UnitGroup findByIdAndStatus(UUID unitGroupId, boolean statusTrue);

    UnitGroup findByNameAndUnitGroupTypeAndStatus(String name, String unitGroupType, boolean status);

    List<UnitGroup> findAllByStatus(boolean statusTrue);

    @Query("SELECT u FROM UnitGroup u WHERE u.name like ?2 AND u.status = true AND u.id <> ?1")
    Optional<UnitGroup> checkExist(UUID id, String name);
}
