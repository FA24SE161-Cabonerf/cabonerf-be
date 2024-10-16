package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {
    @Query("SELECT c FROM Connector c WHERE c.endProcess.id = ?1")
    Connector checkExist(long endProcessId);
}
