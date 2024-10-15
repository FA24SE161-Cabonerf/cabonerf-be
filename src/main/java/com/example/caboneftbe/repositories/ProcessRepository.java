package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.Process;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    @Query("SELECT p FROM Process p WHERE p.project.id = ?1")
    List<Process> findAllByProjectId(long projectId);
    @Query("SELECT p FROM Process p WHERE p.project.id = ?1")
    Page<Process> findAllByProjectIdWithPage(long projectId, Pageable pageable);
}
