package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.LifeCycleStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeCycleStageRepository extends JpaRepository<LifeCycleStage, Long> {
}
