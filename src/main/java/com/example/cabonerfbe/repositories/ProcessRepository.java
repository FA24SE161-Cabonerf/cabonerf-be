package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Process;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessRepository extends JpaRepository<Process, UUID> {
    @Query("SELECT DISTINCT p FROM Process p JOIN FETCH p.project pj WHERE p.project.id = ?1 AND p.status = true")
    List<Process> findAll(UUID projectId);
    @Query("SELECT p FROM Process p WHERE p.name like ?1 AND p.project.id = ?2")
    Optional<Process> findByName(String name,UUID projectId);
    @Query("SELECT p FROM Process p WHERE p.id = ?1 AND p.status = true")
    Optional<Process> findByProcessId(UUID id);
}
