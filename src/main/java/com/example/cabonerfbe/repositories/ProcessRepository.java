package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Process;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Process repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ProcessRepository extends JpaRepository<Process, UUID> {
    /**
     * Find all method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("SELECT DISTINCT p FROM Process p JOIN FETCH p.project pj WHERE p.project.id = ?1 AND p.status = true")
    List<Process> findAll(UUID projectId);

    /**
     * Find by name method.
     *
     * @param name      the name
     * @param projectId the project id
     * @return the optional
     */
    @Query("SELECT p FROM Process p WHERE p.name like ?1 AND p.project.id = ?2")
    Optional<Process> findByName(String name, UUID projectId);

    /**
     * Find by process id method.
     *
     * @param id the id
     * @return the optional
     */
    @Query("SELECT p FROM Process p WHERE p.id = ?1 AND p.status = true")
    Optional<Process> findByProcessId(UUID id);

    /**
     * Find all with created asc method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("SELECT DISTINCT p FROM Process p JOIN FETCH p.project pj WHERE p.project.id = ?1 AND p.status = true ORDER BY p.createdAt asc")
    List<Process> findAllWithCreatedAsc(UUID projectId);

    /**
     * Find processes without outgoing connectors method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("""
                SELECT p 
                FROM Process p 
                LEFT JOIN Connector c ON p.id = c.startProcess.id 
                WHERE c.startProcess.id IS NULL 
                  AND p.status = true 
                  AND p.project.id = :projectId
            """)
    List<Process> findProcessesWithoutOutgoingConnectors(@Param("projectId") UUID projectId);

    @Query("""
                SELECT (count(*))
                FROM Process p 
                LEFT JOIN Connector c ON p.id = c.startProcess.id 
                WHERE c.startProcess.id IS NULL 
                  AND p.status = true 
                  AND p.project.id = :projectId
            """)
    Long countProcessesWithoutOutgoingConnectors(@Param("projectId") UUID projectId);


    /**
     * Find root process method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("SELECT p FROM Process p " +
            "WHERE p.project.id = :projectId " +
            "AND p.id IN (SELECT c.endProcess.id FROM Connector c WHERE c.endProcess.project.id = :projectId AND c.status = true) " +
            "AND p.id NOT IN (SELECT c.startProcess.id FROM Connector c WHERE c.startProcess.project.id = :projectId AND c.status = true)" +
            "AND p.status = true")
    List<Process> findRootProcess(@Param("projectId") UUID projectId);

    /**
     * Find object library method.
     *
     * @param organizationId   the organization id
     * @param systemBoundaryId the system boundary id
     * @param keyword          the keyword
     * @param pageable         the pageable
     * @return the page
     */
    @Query("SELECT p FROM Process p " +
            "WHERE p.organization.id = :organizationId AND p.status = true AND p.project is NULL " +
            "AND (:systemBoundaryId IS NULL OR p.systemBoundary.id = :systemBoundaryId) " +
            "AND (COALESCE(:keyword, '') = '' OR p.name ILIKE CONCAT('%', :keyword, '%'))")
    Page<Process> findObjectLibrary(@Param("organizationId") UUID organizationId, @Param("systemBoundaryId") UUID systemBoundaryId, @Param("keyword") String keyword, Pageable pageable);

    /**
     * Count all by project id method.
     *
     * @param id the id
     * @return the boolean
     */
    @Query("select count(*)>20 from Process p where p.project.id = ?1 and p.status = true")
    boolean countAllByProject_Id(UUID id);

    /**
     * Find by process id and library method.
     *
     * @param id      the id
     * @param library the library
     * @return the optional
     */
    @Query("SELECT p FROM Process p WHERE p.id = :id AND p.status = true AND p.library = :is_library")
    Optional<Process> findByProcessIdAndLibrary(@Param("id") UUID id, @Param("is_library") boolean library);

    /**
     * Find all object library method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("SELECT p FROM Process p WHERE p.library = true AND p.status = true")
    List<Process> findAllObjectLibrary(UUID projectId);

    /**
     * Find dataset method.
     *
     * @param datasetList the dataset list
     * @param pageable    the pageable
     * @return the page
     */
    @Query("SELECT p FROM Process p WHERE p.organization.id IN :dataIds AND p.status = true")
    Page<Process> findDataset(@Param("dataIds") List<UUID> datasetList, Pageable pageable);

    /**
     * Find dataset by keword method.
     *
     * @param datasetList the dataset list
     * @param keyword     the keyword
     * @param pageable    the pageable
     * @return the page
     */
    @Query("SELECT p FROM Process p WHERE p.organization.id IN :dataIds AND p.name ILIKE CONCAT('%', :keyword, '%') AND p.status = true")
    Page<Process> findDatasetByKeword(@Param("dataIds") List<UUID> datasetList, @Param("keyword") String keyword, Pageable pageable);
}
