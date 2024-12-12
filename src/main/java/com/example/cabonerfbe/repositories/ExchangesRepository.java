package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.Exchanges;
import com.example.cabonerfbe.models.UnitGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Exchanges repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ExchangesRepository extends JpaRepository<Exchanges, UUID> {
    /**
     * Find all by process method.
     *
     * @param processId the process id
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.status = true ORDER BY e.createdAt asc ")
    List<Exchanges> findAllByProcess(UUID processId);

    /**
     * Find product out method.
     *
     * @param processId the process id
     * @return the optional
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.input = false AND e.exchangesType.name like 'Product' AND e.status = true")
    Optional<Exchanges> findProductOut(UUID processId);

    /**
     * Find elementary method.
     *
     * @param processId              the process id
     * @param substanceCompartmentId the substance compartment id
     * @return the optional
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = :processId AND e.emissionSubstance.id = :substanceCompartmentId AND e.status = true")
    Optional<Exchanges> findElementary(@Param("processId") UUID processId, @Param("substanceCompartmentId") UUID substanceCompartmentId);

    /**
     * Find by id and type and input method.
     *
     * @param exchangeId the exchange id
     * @param type       the type
     * @param isInput    the is input
     * @return the optional
     */
    @Query("SELECT e FROM Exchanges e WHERE e.id = :exchangeId AND e.status = true AND e.exchangesType.name like :type AND e.input = :isInput")
    Optional<Exchanges> findByIdAndTypeAndInput(@Param("exchangeId") UUID exchangeId, @Param("type") String type, @Param("isInput") boolean isInput);

    /**
     * Find by process id and name and unit unit group and input method.
     *
     * @param id        the id
     * @param name      the name
     * @param unitGroup the unit group
     * @param input     the input
     * @return the optional
     */
    @Query("select e from Exchanges e where e.process.id = ?1 and e.name = ?2 and e.unit.unitGroup = ?3 and e.input = ?4 and e.status = true")
    Optional<Exchanges> findByProcess_IdAndNameAndUnit_UnitGroupAndInput(UUID id, String name, UnitGroup unitGroup, boolean input);

    /**
     * Find by id and status method.
     *
     * @param id     the id
     * @param status the status
     * @return the optional
     */
    Optional<Exchanges> findByIdAndStatus(UUID id, boolean status);

    /**
     * Find all by process id and exchanges type method.
     *
     * @param id            the id
     * @param exchangesType the exchanges type
     * @return the list
     */
    @Query("select e from Exchanges e where e.process.id = ?1 and e.exchangesType.name = ?2 and e.status = true")
    List<Exchanges> findAllByProcessIdAndExchangesType(UUID id, String exchangesType);

    /**
     * Find product by process id method.
     *
     * @param currentProcessId the current process id
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.exchangesType.name like 'Product'AND e.status = true")
    List<Exchanges> findProductByProcessId(UUID currentProcessId);

    /**
     * Find all by id matches method.
     *
     * @param exchangeIdList the exchange id list
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.id IN :exchangeIdList AND e.status = true")
    List<Exchanges> findAllByIdMatches(@Param("exchangeIdList") List<UUID> exchangeIdList);

    /**
     * Find elementary exchange by project method.
     *
     * @param projectId the project id
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.project.id = :projectId AND e.status = true AND e.exchangesType.name like 'Elementary'")
    List<Exchanges> findElementaryExchangeByProject(@Param("projectId") UUID projectId);

    /**
     * Find all by process ids input method.
     *
     * @param processIds the process ids
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id IN :processIds AND e.status = true AND e.input = true")
    List<Exchanges> findAllByProcessIdsInput(@Param("processIds") List<UUID> processIds);

    /**
     * Find product in method.
     *
     * @param id the id
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.status = true AND e.process.id = :processId AND e.input = true AND e.exchangesType.name like 'Product'")
    List<Exchanges> findProductIn(@Param("processId") UUID id);

    /**
     * Find product out with one process method.
     *
     * @param id the id
     * @return the exchanges
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.input = false AND e.exchangesType.name like 'Product' AND e.status = true")
    Exchanges findProductOutWithOneProcess(UUID id);

    /**
     * Find elementary by process method.
     *
     * @param id the id
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.exchangesType.name like 'Elementary' AND e.status = true")
    List<Exchanges> findElementaryByProcess(UUID id);

    /**
     * Find all by elementary method.
     *
     * @param processIds the process ids
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id IN :processIds AND e.exchangesType.name like 'Elementary' AND e.status = true")
    List<Exchanges> findAllByElementary(@Param("processIds") List<UUID> processIds);

    /**
     * Exist all by elementary method.
     *
     * @param processIds the process ids
     * @return the boolean
     */
    @Query("SELECT (count(*) > 0) FROM Exchanges e WHERE e.process.id IN :processIds AND e.exchangesType.name like 'Elementary' AND e.status = true")
    boolean existAllByElementary(@Param("processIds") List<UUID> processIds);

    /**
     * Find all by process join fetch method.
     *
     * @param processId the process id
     * @return the list
     */
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.status = true ORDER BY e.createdAt asc ")
    List<Exchanges> findAllByProcessJoinFetch(UUID processId);

    /**
     * Find all by process ids and exchanges type method.
     *
     * @param processIds         the process ids
     * @param elementaryExchange the elementary exchange
     * @return the list
     */
    @Query("select e from Exchanges e where e.process.id IN :processIds and e.exchangesType.name = :elementaryExchange and e.status = true")
    List<Exchanges> findAllByProcessIdsAndExchangesType(@Param("processIds") List<UUID> processIds, @Param("elementaryExchange") String elementaryExchange);
}
