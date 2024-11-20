package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessRepository extends JpaRepository<Process, UUID> {
    @Query("SELECT DISTINCT p FROM Process p JOIN FETCH p.project pj WHERE p.project.id = ?1 AND p.status = true")
    List<Process> findAll(UUID projectId);

    @Query("SELECT p FROM Process p WHERE p.name like ?1 AND p.project.id = ?2")
    Optional<Process> findByName(String name, UUID projectId);

    @Query("SELECT p FROM Process p WHERE p.id = ?1 AND p.status = true")
    Optional<Process> findByProcessId(UUID id);

    @Query("SELECT DISTINCT p FROM Process p JOIN FETCH p.project pj WHERE p.project.id = ?1 AND p.status = true ORDER BY p.createdAt asc")
    List<Process> findAllWithCreatedAsc(UUID projectId);

    @Query("""
                SELECT p 
                FROM Process p 
                LEFT JOIN Connector c ON p.id = c.startProcess.id 
                WHERE c.startProcess.id IS NULL 
                  AND p.status = true 
                  AND p.project.id = :projectId
            """)
    List<Process> findProcessesWithoutOutgoingConnectors(@Param("projectId") UUID projectId);


    @Query("SELECT p FROM Process p " +
            "WHERE p.project.id = :projectId " +
            "AND p.id IN (SELECT c.endProcess.id FROM Connector c WHERE c.endProcess.project.id = :projectId) " +
            "AND p.id NOT IN (SELECT c.startProcess.id FROM Connector c WHERE c.startProcess.project.id = :projectId)")
    List<Process> findRootProcess(@Param("projectId") UUID projectId);
}
