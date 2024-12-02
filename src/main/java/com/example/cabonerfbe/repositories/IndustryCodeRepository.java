package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.IndustryCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true ")
    Page<IndustryCode> findAll(Pageable pageable);

    @Query("SELECT ic FROM IndustryCode ic WHERE ic.status = true AND ic.name ILIKE CONCAT('%', :keyword ,'%')")
    Page<IndustryCode> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT ic FROM IndustryCode ic WHERE ic.code like :code")
    Optional<IndustryCode> findByCode(@Param("code") String code);

    boolean existsByCodeAndIdNot(String code, UUID id);
}
