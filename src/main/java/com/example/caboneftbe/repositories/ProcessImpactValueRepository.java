package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.ProcessImpactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessImpactValueRepository extends JpaRepository<ProcessImpactValue, Long> {
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1")
    List<ProcessImpactValue> findByProcessId(long id);
}
