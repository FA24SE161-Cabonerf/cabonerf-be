package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Exchanges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExchangesRepository extends JpaRepository<Exchanges, UUID> {
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1")
    List<Exchanges> findAllByProcess(UUID processId);
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.input = false")
    Exchanges findByProcess(UUID processId);
}
