package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
}