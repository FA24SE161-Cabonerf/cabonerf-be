package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

    @Query("SELECT w FROM Workspace w JOIN FETCH w.owner o WHERE o.id = :userId")
    List<Workspace> findByUser(@Param("userId") UUID userId);
}
