package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.ProcessImpactValue;
import com.example.cabonerfbe.models.ProjectImpactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public interface ProcessImpactValueRepository extends JpaRepository<ProcessImpactValue, UUID> {
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 ORDER BY p.impactMethodCategory.impactCategory.name asc")
    List<ProcessImpactValue> findByProcessId(UUID id);

    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 AND p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = ?2")
    List<ProcessImpactValue> findByProcessAndMethod(UUID id, UUID methodId);

    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 AND p.impactMethodCategory.id = ?2 AND p.status = true")
    Optional<ProcessImpactValue> findByProcessIdAndImpactMethodCategoryId(UUID processId, UUID methodCategoryId);

    @Query("SELECT piv FROM ProcessImpactValue piv WHERE piv.process.project.id = ?1 AND piv.impactMethodCategory.id = ?2 AND piv.status = true")
    List<ProcessImpactValue> findByProjectIdAndImpactMethodCategoryId(UUID projectId, UUID impactMethodCategoryId);

    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id IN :processIds OR p.process.id IN :processIds AND p.status = true AND p.impactMethodCategory.id = :imcId")
    List<ProcessImpactValue> findAllByProcessIdsAAndImpactMethodCategory(@Param("processIds") List<UUID> processIds, @Param("imcId") UUID imcId);

    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id IN :processIds OR p.process.id IN :processIds AND p.status = true")
    List<ProcessImpactValue> findAllByProcessIds(@Param("processIds") List<UUID> processIds);


    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = :processId AND p.status = true ")
    List<ProcessImpactValue> findAllByProcessId(@Param("processId") UUID processId);

    @Query("SELECT piv FROM ProcessImpactValue piv WHERE piv.process = :process")
    List<ProcessImpactValue> findAllByProcess(@Param("process") Process process);

    @Modifying
    @Transactional
    @Query("UPDATE ProcessImpactValue p SET p.previousProcessValue = 0 WHERE p.process.id IN :processIds OR p.process.id IN :processIds AND p.status = true ")
    void setDefaultPrevious(@Param("processIds") List<UUID> processIds);

    @Query("SELECT p FROM ProjectImpactValue p WHERE p.project.id = :projectId AND p.impactMethodCategory.impactCategory.name like 'Climate Change'")
    ProjectImpactValue findCO2(@Param("projectId") UUID projectId);

    @Modifying
    @Transactional
    @Query("UPDATE ProcessImpactValue p SET p.systemLevel = 0 WHERE p.process.id = :processId AND p.status = true ")
    void setDefaultSystemLevel(@Param("processId") UUID processId);

    @Query("SELECT p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id " +
            "FROM ProcessImpactValue p " +
            "WHERE p.process.id = :processId " +
            "GROUP BY p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id")
    UUID findMethodProcessUse(@Param("processId") UUID processId);

    @Query("SELECT p.process.id AS processId, " +
            "       p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id AS methodId " +
            "FROM ProcessImpactValue p " +
            "WHERE p.process.id IN :processIds " +
            "GROUP BY p.process.id, p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id")
    List<Object[]> findRawMethodIdsForProcesses(@Param("processIds") List<UUID> processIds);

    default Map<UUID, UUID> findMethodIdsForProcesses(List<UUID> processIds) {
        return findRawMethodIdsForProcesses(processIds).stream()
                .collect(Collectors.toMap(
                        result -> (UUID) result[0], // processId
                        result -> (UUID) result[1]  // methodId
                ));
    }

    @Query("SELECT p " +
            "FROM ProcessImpactValue p " +
            "WHERE p.process.id IN :processIds " +
            "AND p.impactMethodCategory.impactCategory.id = :categoryId " +
            "AND p.process.lifeCycleStage.id = :lifeCycleStageId")
    List<ProcessImpactValue> getPercent(@Param("categoryId") UUID categoryId,
                                        @Param("lifeCycleStageId") UUID lifeCycleStageId,
                                        @Param("processIds") List<UUID> processIds);

    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id IN :processIds AND p.impactMethodCategory.impactCategory.id = :categoryId AND p.status = true")
    List<ProcessImpactValue> findAllByProcessIdsAndCategory(@Param("processIds") List<UUID> processIds,@Param("categoryId") UUID categoryId);
}
