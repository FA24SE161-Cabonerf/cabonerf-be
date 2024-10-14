package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.ExchangesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangesTypeRepository extends JpaRepository<ExchangesType, Long> {
}