package com.example.specification;

import com.example.cabonerfbe.models.EmissionSubstances;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmissionSubstancesSpecification {
    public static Specification<EmissionSubstances> containsKeywordInAllFields(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("chemicalName")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("molecularFormula")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("alternativeFormula")), pattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
