package com.example.cabonerfbe.repositories;

import com.example.cabonerfbe.enums.QueryStrings;
import com.example.cabonerfbe.models.ImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImpactCategoryRepository extends JpaRepository<ImpactCategory, UUID> {
    @Query("SELECT a FROM ImpactCategory a WHERE UPPER(a.name) LIKE CONCAT('%', ?1, '%')")
    ImpactCategory findByName(String name);

    List<ImpactCategory> findAllByStatus(boolean status);

    @Query(QueryStrings.FIND_CATEGORY_BY_IMPACT_METHOD_ID)
    List<ImpactCategory> findAllByImpactMethodId(@Param("methodId") UUID methodId);

    Optional<ImpactCategory> findByIdAndStatus(UUID categoryId, boolean statusTrue);


}
