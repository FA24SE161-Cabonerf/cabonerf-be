package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.enums.QueryStrings;
import com.example.cabonerfbe.models.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, UUID> {
    @Query("SELECT c FROM Connector c WHERE c.endProcess.id = ?1")
    Connector checkExist(UUID endProcessId);

    @Query("SELECT c FROM Connector c WHERE c.startProcess.project.id = ?1")
    List<Connector> findAllByProject(UUID projectId);

    @Query(QueryStrings.CONNECTOR_EXIST_BY_START_END_PROCESS)
    boolean existsByStartProcess_IdAndEndProcess_Id(UUID id, UUID id1);

    @Query("SELECT c FROM Connector c WHERE c.startProcess.id IN :processIds OR c.endProcess.id IN :processIds AND c.status = true")
    List<Connector> findAllByProcessIds(@Param("processIds") List<UUID> processIds);

    @Query("SELECT c FROM Connector c WHERE c.startProcess.id = ?1 AND c.status = true")
    List<Connector> findNextByStartProcessId(UUID currentProcessId);

    @Query("SELECT c FROM Connector c WHERE c.endExchanges.id = ?1 OR c.startExchanges.id = ?1 and c.status = true")
    List<Connector> findConnectorToExchange(UUID exchangeId);
}
