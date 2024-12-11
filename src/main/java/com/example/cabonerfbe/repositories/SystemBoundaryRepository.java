package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.SystemBoundary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface System boundary repository.
 *
 * @author SonPHH.
 */
public interface SystemBoundaryRepository extends JpaRepository<SystemBoundary, UUID> {
    /**
     * Find by from and to method.
     *
     * @param sysBoundaryFrom the sys boundary from
     * @param sysBoundaryTo   the sys boundary to
     * @return the optional
     */
    @Query("SELECT s from SystemBoundary s WHERE s.boundaryFrom = :from AND s.boundaryTo = :to AND s.status = true")
    Optional<SystemBoundary> findByFromAndTo(@Param("from") String sysBoundaryFrom, @Param("to") String sysBoundaryTo);

    /**
     * Find all true method.
     *
     * @return the list
     */
    @Query("SELECT s from SystemBoundary s WHERE s.status = true")
    List<SystemBoundary> findAllTrue();
}