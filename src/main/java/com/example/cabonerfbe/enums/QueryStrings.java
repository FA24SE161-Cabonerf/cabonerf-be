package com.example.cabonerfbe.enums;

public class QueryStrings {
    public static final String FIND_CATEGORY_BY_IMPACT_METHOD_ID = "SELECT ic FROM ImpactCategory ic " +
            "JOIN ImpactMethodCategory imc ON ic.id = imc.impactCategory.id " +
            "WHERE imc.lifeCycleImpactAssessmentMethod.id = :methodId " +
            "AND ic.status = true " +
            "AND imc.status = true ";

    public static final String FIND_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVES =
            "SELECT \n" +
                    "sc.id,\n" +
                    "f.cas,\n" +
                    "es.name,\n" +
                    "es.chemical_name,\n" +
                    "es.molecular_formula,\n" +
                    "es.alternative_formula,\n" +
                    "ec.name AS compartment_name,\n" +
                    "MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist,\n" +
                    "MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist,\n" +
                    "MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian \n" +
                    "FROM substances_compartments sc \n" +
                    "JOIN emission_substances es ON sc.emission_substance_id = es.id AND es.status = true \n" +
                    "JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true \n" +
                    "JOIN midpoint_impact_characterization_factors f ON sc.id = f.substances_compartments_id AND f.status = true \n" +
                    "JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true \n" +
                    "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true \n" +
                    "JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true \n" +
                    "WHERE sc.status = true \n" +
                    "GROUP BY sc.id, f.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name \n" +
                    "ORDER BY sc.id ASC;";

    public static final String COUNT_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVE = """
    SELECT 
        COUNT(*) 
    FROM (
        SELECT 
            f.cas, 
            es.name, 
            es.chemical_name, 
            es.molecular_formula,
            es.alternative_formula,
            ec.name AS compartment_name, 
            MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist, 
            MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist, 
            MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian
        FROM substances_compartments sc 
        JOIN emission_substances es ON sc.emission_substance_id = es.id AND es.status = true 
        JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true 
        JOIN midpoint_impact_characterization_factors f ON sc.id = f.substances_compartments_id AND f.status = true 
        JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true 
        JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true 
        JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true 
        WHERE sc.status = true 
        GROUP BY sc.id, f.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name
    ) AS subquery;
    """;


}

