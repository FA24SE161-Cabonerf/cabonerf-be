package com.example.caboneftbe.repositories;

import com.example.caboneftbe.enums.QueryConstants;
import com.example.caboneftbe.models.ImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImpactCategoryRepository extends JpaRepository<ImpactCategory, Long> {
    List<ImpactCategory> findAllByStatus(boolean status);
    @Query(QueryConstants.FIND_CATEGORY_BY_IMPACT_METHOD_ID)
    List<ImpactCategory> findAllByImpactMethodId(@Param("methodId") long methodId);
}
