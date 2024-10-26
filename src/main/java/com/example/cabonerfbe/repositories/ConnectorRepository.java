package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, UUID> {
    @Query("SELECT c FROM Connector c WHERE c.endProcess.id = ?1")
    Connector checkExist(UUID endProcessId);

    @Query("SELECT c FROM Connector c WHERE c.startProcess.project.id = ?1")
    List<Connector> findAllByProject(UUID projectId);
}
