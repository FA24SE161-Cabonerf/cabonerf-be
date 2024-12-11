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

/**
 * The interface Process impact value repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ProcessImpactValueRepository extends JpaRepository<ProcessImpactValue, UUID> {
    /**
     * Find by process id method.
     *
     * @param id the id
     * @return the list
     */
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 ORDER BY p.impactMethodCategory.impactCategory.name asc")
    List<ProcessImpactValue> findByProcessId(UUID id);

    /**
     * Find by process and method method.
     *
     * @param id       the id
     * @param methodId the method id
     * @return the list
     */
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 AND p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id = ?2")
    List<ProcessImpactValue> findByProcessAndMethod(UUID id, UUID methodId);

    /**
     * Find by process id and impact method category id method.
     *
     * @param processId        the process id
     * @param methodCategoryId the method category id
     * @return the optional
     */
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = ?1 AND p.impactMethodCategory.id = ?2 AND p.status = true")
    Optional<ProcessImpactValue> findByProcessIdAndImpactMethodCategoryId(UUID processId, UUID methodCategoryId);

    /**
     * Find by project id and impact method category id method.
     *
     * @param projectId              the project id
     * @param impactMethodCategoryId the impact method category id
     * @return the list
     */
    @Query("SELECT piv FROM ProcessImpactValue piv WHERE piv.process.project.id = ?1 AND piv.impactMethodCategory.id = ?2 AND piv.status = true")
    List<ProcessImpactValue> findByProjectIdAndImpactMethodCategoryId(UUID projectId, UUID impactMethodCategoryId);

    /**
     * Find all by process ids a and impact method category method.
     *
     * @param processIds the process ids
     * @param imcId      the imc id
     * @return the list
     */
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id IN :processIds OR p.process.id IN :processIds AND p.status = true AND p.impactMethodCategory.id = :imcId")
    List<ProcessImpactValue> findAllByProcessIdsAAndImpactMethodCategory(@Param("processIds") List<UUID> processIds, @Param("imcId") UUID imcId);

    /**
     * Find all by process ids method.
     *
     * @param processIds the process ids
     * @return the list
     */
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id IN :processIds OR p.process.id IN :processIds AND p.status = true")
    List<ProcessImpactValue> findAllByProcessIds(@Param("processIds") List<UUID> processIds);


    /**
     * Find all by process id method.
     *
     * @param processId the process id
     * @return the list
     */
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id = :processId AND p.status = true ")
    List<ProcessImpactValue> findAllByProcessId(@Param("processId") UUID processId);

    /**
     * Find all by process method.
     *
     * @param process the process
     * @return the list
     */
    @Query("SELECT piv FROM ProcessImpactValue piv WHERE piv.process = :process")
    List<ProcessImpactValue> findAllByProcess(@Param("process") Process process);

    /**
     * Sets default previous.
     *
     * @param processIds the process ids
     */
    @Modifying
    @Transactional
    @Query("UPDATE ProcessImpactValue p SET p.previousProcessValue = 0 WHERE p.process.id IN :processIds OR p.process.id IN :processIds AND p.status = true ")
    void setDefaultPrevious(@Param("processIds") List<UUID> processIds);

    /**
     * Find co 2 method.
     *
     * @param projectId the project id
     * @return the project impact value
     */
    @Query("SELECT p FROM ProjectImpactValue p WHERE p.project.id = :projectId AND p.impactMethodCategory.impactCategory.name like 'Climate Change'")
    ProjectImpactValue findCO2(@Param("projectId") UUID projectId);

    /**
     * Sets default system level.
     *
     * @param processId the process id
     */
    @Modifying
    @Transactional
    @Query("UPDATE ProcessImpactValue p SET p.systemLevel = 0 WHERE p.process.id = :processId AND p.status = true ")
    void setDefaultSystemLevel(@Param("processId") UUID processId);

    /**
     * Find method process use method.
     *
     * @param processId the process id
     * @return the uuid
     */
    @Query("SELECT p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id " +
            "FROM ProcessImpactValue p " +
            "WHERE p.process.id = :processId " +
            "GROUP BY p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id")
    UUID findMethodProcessUse(@Param("processId") UUID processId);

    /**
     * Find raw method ids for processes method.
     *
     * @param processIds the process ids
     * @return the list
     */
    @Query("SELECT p.process.id AS processId, " +
            "       p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id AS methodId " +
            "FROM ProcessImpactValue p " +
            "WHERE p.process.id IN :processIds " +
            "GROUP BY p.process.id, p.impactMethodCategory.lifeCycleImpactAssessmentMethod.id")
    List<Object[]> findRawMethodIdsForProcesses(@Param("processIds") List<UUID> processIds);

    /**
     * Find method ids for processes method.
     *
     * @param processIds the process ids
     * @return the map
     */
    default Map<UUID, UUID> findMethodIdsForProcesses(List<UUID> processIds) {
        return findRawMethodIdsForProcesses(processIds).stream()
                .collect(Collectors.toMap(
                        result -> (UUID) result[0], // processId
                        result -> (UUID) result[1]  // methodId
                ));
    }

    /**
     * Gets percent.
     *
     * @param categoryId       the category id
     * @param lifeCycleStageId the life cycle stage id
     * @param processIds       the process ids
     * @return the percent
     */
    @Query("SELECT p " +
            "FROM ProcessImpactValue p " +
            "WHERE p.process.id IN :processIds " +
            "AND p.impactMethodCategory.impactCategory.id = :categoryId " +
            "AND p.process.lifeCycleStage.id = :lifeCycleStageId")
    List<ProcessImpactValue> getPercent(@Param("categoryId") UUID categoryId,
                                        @Param("lifeCycleStageId") UUID lifeCycleStageId,
                                        @Param("processIds") List<UUID> processIds);

    /**
     * Find all by process ids and category method.
     *
     * @param processIds the process ids
     * @param categoryId the category id
     * @return the list
     */
    @Query("SELECT p FROM ProcessImpactValue p WHERE p.process.id IN :processIds AND p.impactMethodCategory.impactCategory.id = :categoryId AND p.status = true")
    List<ProcessImpactValue> findAllByProcessIdsAndCategory(@Param("processIds") List<UUID> processIds, @Param("categoryId") UUID categoryId);
}
