package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.models.LifeCycleStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeCycleStageRepository extends JpaRepository<LifeCycleStage, Long> {
}
