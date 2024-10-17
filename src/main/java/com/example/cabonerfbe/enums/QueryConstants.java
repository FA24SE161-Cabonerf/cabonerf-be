package com.example.cabonerfbe.enums;

public class QueryConstants {
    public static final String FIND_CATEGORY_BY_IMPACT_METHOD_ID = "SELECT ic FROM ImpactCategory ic " +
            "JOIN ImpactMethodCategory imc ON ic.id = imc.impactCategory.id " +
            "WHERE imc.lifeCycleImpactAssessmentMethod.id = :methodId";

    public static final String FIND_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVES = "";
}

