package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.enums.QueryStrings;
import com.example.cabonerfbe.models.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The interface Connector repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ConnectorRepository extends JpaRepository<Connector, UUID> {
    /**
     * Check exist method.
     *
     * @param endProcessId the end process id
     * @return the connector
     */
    @Query("SELECT c FROM Connector c WHERE c.endProcess.id = ?1")
    Connector checkExist(UUID endProcessId);

    /**
     * Find all by project method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("SELECT c FROM Connector c WHERE c.startProcess.project.id = ?1 AND c.status = true")
    List<Connector> findAllByProject(UUID projectId);

    /**
     * Exists by start process id and end process id method.
     *
     * @param id  the id
     * @param id1 the id 1
     * @return the boolean
     */
    @Query(QueryStrings.CONNECTOR_EXIST_BY_START_END_PROCESS)
    boolean existsByStartProcess_IdAndEndProcess_Id(UUID id, UUID id1);

    /**
     * Find all by process ids method.
     *
     * @param processIds the process ids
     * @return the set
     */
    @Query("SELECT c FROM Connector c WHERE c.startProcess.id IN :processIds OR c.endProcess.id IN :processIds AND c.status = true")
    Set<Connector> findAllByProcessIds(@Param("processIds") List<UUID> processIds);

    /**
     * Find next by start process id method.
     *
     * @param currentProcessId the current process id
     * @return the list
     */
    @Query("SELECT c FROM Connector c WHERE c.startProcess.id = ?1 AND c.status = true")
    List<Connector> findNextByStartProcessId(UUID currentProcessId);

    /**
     * Find connector to exchange method.
     *
     * @param exchangeId the exchange id
     * @return the list
     */
    @Query("SELECT c FROM Connector c WHERE (c.endExchanges.id = ?1 OR c.startExchanges.id = ?1) and c.status = true")
    List<Connector> findConnectorToExchange(UUID exchangeId);

    /**
     * Find connector to exchange not library method.
     *
     * @param exchangeId the exchange id
     * @return the list
     */
    @Query("SELECT c FROM Connector c WHERE (c.endExchanges.id = ?1 OR c.startExchanges.id = ?1) and c.startProcess.library = false and c.endProcess.library = false and c.status = true")
    List<Connector> findConnectorToExchangeNotLibrary(UUID exchangeId);

    /**
     * Find connector to process method.
     *
     * @param processId the process id
     * @return the list
     */
    @Query("SELECT c FROM Connector c WHERE (c.endProcess.id = ?1 OR c.startProcess.id = ?1) and c.status = true")
    List<Connector> findConnectorToProcess(UUID processId);

    /**
     * Exists by end exchanges id method.
     *
     * @param endExchangeId the end exchange id
     * @return the boolean
     */
    @Query("SELECT (count(c) > 0) FROM Connector c WHERE c.endExchanges.id = ?1 AND c.status = true")
    boolean existsByEndExchanges_Id(UUID endExchangeId);

    /**
     * Find by id and status method.
     *
     * @param id     the id
     * @param status the status
     * @return the optional
     */
    @Query("SELECT c FROM Connector c WHERE c.id = ?1 and c.status = ?2")
    Optional<Connector> findByIdAndStatus(UUID id, boolean status);

    /**
     * Find next by start process id one method.
     *
     * @param currentProcessId the current process id
     * @return the connector
     */
    @Query("SELECT c FROM Connector c WHERE c.startProcess.id = ?1 AND c.status = true")
    Connector findNextByStartProcessIdOne(UUID currentProcessId);
}