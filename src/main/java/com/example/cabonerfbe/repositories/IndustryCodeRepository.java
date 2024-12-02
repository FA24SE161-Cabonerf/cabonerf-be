package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.IndustryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndustryCodeRepository extends JpaRepository<IndustryCode, UUID> {
    @Query("SELECT ic FROM IndustryCode ic WHERE ic.id = :industryCodeId AND ic.status = true")
    Optional<IndustryCode> findByIdWithStatus(@Param("industryCodeId") UUID industryCodeId);

    @Query("SELECT ic FROM IndustryCode ic WHERE ic.id in :industryCodeIds AND ic.status = true")
    List<IndustryCode> findAllByIds(@Param("industryCodeIds") List<UUID> industryCodeIds);
}
