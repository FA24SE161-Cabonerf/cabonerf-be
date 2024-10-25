package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {
    @Query("SELECT c FROM Connector c WHERE c.endProcess.id = ?1")
    Connector checkExist(long endProcessId);

    @Query("SELECT c FROM Connector c WHERE c.startProcess.project.id = ?1")
    List<Connector> findAllByProject(long projectId);
}
