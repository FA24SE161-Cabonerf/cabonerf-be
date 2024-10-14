package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.Exchanges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangesRepository extends JpaRepository<Exchanges, Long> {
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1")
    List<Exchanges> findAllByProcess(long processId);
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.input = false")
    Exchanges findByProcess(long processId);
}
