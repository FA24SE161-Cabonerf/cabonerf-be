package com.example.cabonerfbe.enums;

public class QueryStrings {
    public static final String FIND_CATEGORY_BY_IMPACT_METHOD_ID = "SELECT ic FROM ImpactCategory ic " +
            "JOIN ImpactMethodCategory imc ON ic.id = imc.impactCategory.id " +
            "WHERE imc.lifeCycleImpactAssessmentMethod.id = :methodId";

    public static final String FIND_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVES =
            "SELECT " +
                    "    micf.cas AS cas_number, " +
                    "    es.name, " +
                    "    es.chemical_name, " +
                    "    es.molecular_formula, " +
                    "    es.alternative_formula, " +
                    "    ec.name AS compartment_name, " +
                    "    MAX(CASE WHEN p.name = 'Individualist' THEN micf.decimal_value END) AS individualist, " +
                    "    MAX(CASE WHEN p.name = 'Hierarchist' THEN micf.decimal_value END) AS hierarchist, " +
                    "    MAX(CASE WHEN p.name = 'Egalitarian' THEN micf.decimal_value END) AS egalitarian " +
                    "FROM " +
                    "    emission_substances es " +
                    "JOIN " +
                    "    midpoint_impact_characterization_factors micf ON micf.emission_substances_id = es.id " +
                    "JOIN " +
                    "    impact_method_category imc ON imc.id = micf.impact_method_category_id " +
                    "JOIN " +
                    "    life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id " +
                    "JOIN " +
                    "    perspective p ON p.id = lciam.perspective_id " +
                    "JOIN " +
                    "    emission_compartment ec ON ec.id = micf.emission_compartment_id " +
                    "GROUP BY " +
                    "    es.id, micf.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name " +
                    "ORDER BY " +
                    "    es.id ASC;";
    public static final String COUNT_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVE = """
                SELECT 
                    COUNT(*) 
                FROM (
                    SELECT 
                        micf.cas AS cas_number,
                        es.name,
                        es.chemical_name,
                        es.molecular_formula,
                        es.alternative_formula,
                        ec.name AS compartment_name,
                        MAX(CASE WHEN p.name = 'Individualist' THEN micf.decimal_value END) AS individualist,
                        MAX(CASE WHEN p.name = 'Hierarchist' THEN micf.decimal_value END) AS hierarchist,
                        MAX(CASE WHEN p.name = 'Egalitarian' THEN micf.decimal_value END) AS egalitarian
                    FROM 
                        emission_substances es
                    JOIN 
                        midpoint_impact_characterization_factors micf ON micf.emission_substances_id = es.id
                    JOIN 
                        impact_method_category imc ON imc.id = micf.impact_method_category_id
                    JOIN 
                        life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id 
                    JOIN 
                        perspective p ON p.id = lciam.perspective_id
                    JOIN 
                        emission_compartment ec ON ec.id = micf.emission_compartment_id
                    GROUP BY 
                        es.id, micf.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name
                ) AS subquery;
            """;

}

