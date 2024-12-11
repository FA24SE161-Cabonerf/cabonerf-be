package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.ExchangesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The interface Exchanges type repository.
 *
 * @author SonPHH.
 */
@Repository
public interface ExchangesTypeRepository extends JpaRepository<ExchangesType, UUID> {
    /**
     * Find by name method.
     *
     * @param name the name
     * @return the exchanges type
     */
    ExchangesType findByName(String name);
}
