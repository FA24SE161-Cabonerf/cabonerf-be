package com.example.caboneftbe.repositories;

import com.example.caboneftbe.models.ImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpactCategoryRepository extends JpaRepository<ImpactCategory,Long> {
    @Query("SELECT a FROM ImpactCategory a WHERE UPPER(a.name) LIKE CONCAT('%', ?1, '%')")
    ImpactCategory findByName(String name);

}
