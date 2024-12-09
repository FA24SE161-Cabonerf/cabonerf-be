package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.SystemBoundary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemBoundaryRepository extends JpaRepository<SystemBoundary, UUID> {
  @Query("SELECT s from SystemBoundary s WHERE s.boundaryFrom = :from AND s.boundaryTo = :to AND s.status = true")
  Optional<SystemBoundary> findByFromAndTo(@Param("from") String sysBoundaryFrom, @Param("to") String sysBoundaryTo);

  @Query("SELECT s from SystemBoundary s WHERE s.status = true")
  List<SystemBoundary> findAllTrue();
}