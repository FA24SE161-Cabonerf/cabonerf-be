package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {

    @Query("SELECT c FROM Contract c WHERE c.id = :contractId AND c.status = true")
    Optional<Contract> findById(@Param("contractId") UUID contractId);
}
