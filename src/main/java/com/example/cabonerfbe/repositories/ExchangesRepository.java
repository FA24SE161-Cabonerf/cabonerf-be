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

@Repository
public interface ExchangesRepository extends JpaRepository<Exchanges, UUID> {
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.status = true ORDER BY e.createdAt asc ")
    List<Exchanges> findAllByProcess(UUID processId);
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = ?1 AND e.input = false AND e.exchangesType.name like 'Product' AND e.status = true")
    Optional<Exchanges> findProductOut(UUID processId);
    @Query("SELECT e FROM Exchanges e WHERE e.process.id = :processId AND e.emissionSubstance.id = :substanceCompartmentId AND e.status = true")
    Optional<Exchanges> findElementary(@Param("processId") UUID processId,@Param("substanceCompartmentId") UUID substanceCompartmentId);
    @Query("SELECT e FROM Exchanges e WHERE e.id = :exchangeId AND e.status = true AND e.exchangesType.name like :type AND e.input = :isInput")
    Optional<Exchanges> findByIdAndTypeAndInput(@Param("exchangeId") UUID exchangeId, @Param("type") String type, @Param("isInput") boolean isInput);
    @Query("select e from Exchanges e where e.process.id = ?1 and e.name = ?2 and e.unit.unitGroup = ?3 and e.input = ?4")
    Optional<Exchanges> findByProcess_IdAndNameAndUnit_UnitGroupAndInput(UUID id, String name, UnitGroup unitGroup, boolean input);
    Optional<Exchanges> findByIdAndStatus(UUID id, boolean status);
    @Query("select e from Exchanges e where e.process.id = ?1 and e.exchangesType.name = ?2 and e.status = true")
    List<Exchanges> findAllByProcessIdAndExchangesType(UUID id, String exchangesType);
}
