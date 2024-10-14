package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.ImpactCategory;
import com.example.caboneftbe.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query("SELECT a FROM Unit a WHERE a.name LIKE CONCAT('%', ?1, '%')")
    List<Unit> findByName(String name);

    @Query("SELECT u FROM Unit u WHERE u.name like ?1 ")
    Unit findByNameUnit(String name);
}