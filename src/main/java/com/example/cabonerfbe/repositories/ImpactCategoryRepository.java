package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.enums.QueryStrings;
import com.example.cabonerfbe.models.ImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ImpactCategoryRepository extends JpaRepository<ImpactCategory,Long> {
    @Query("SELECT a FROM ImpactCategory a WHERE UPPER(a.name) LIKE CONCAT('%', ?1, '%')")
    ImpactCategory findByName(String name);

    List<ImpactCategory> findAllByStatus(boolean status);
    @Query(QueryStrings.FIND_CATEGORY_BY_IMPACT_METHOD_ID)
    List<ImpactCategory> findAllByImpactMethodId(@Param("methodId") long methodId);
}
