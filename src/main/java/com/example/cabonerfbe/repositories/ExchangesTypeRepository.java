package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.ExchangesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExchangesTypeRepository extends JpaRepository<ExchangesType, UUID> {
}
