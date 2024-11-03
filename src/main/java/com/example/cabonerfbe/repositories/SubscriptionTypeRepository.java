package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, UUID> {
    Optional<SubscriptionType> findBySubscriptionName(String name);
}
