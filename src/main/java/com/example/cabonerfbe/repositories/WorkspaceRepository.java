package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
