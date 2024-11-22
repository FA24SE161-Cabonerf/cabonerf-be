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
                    "es.cas,\n" +
                    "es.name,\n" +
                    "es.chemical_name,\n" +
                    "es.molecular_formula,\n" +
                    "es.alternative_formula,\n" +
                    "ec.name AS compartment_name,\n" +
                    "MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist,\n" +
                    "MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist,\n" +
                    "MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian \n" +
                    "FROM emission_substance sc \n" +
                    "JOIN substance es ON sc.substance_id = es.id AND es.status = true \n" +
                    "LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true \n" +
                    "JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true \n" +
                    "JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true \n" +
                    "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true \n" +
                    "JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true \n" +
                    "WHERE sc.status = true \n" +
                    "GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name \n" +
                    "ORDER BY es.name ASC;";

    public static final String COUNT_MIDPOINT_SUBSTANCE_FACTORS_WITH_PERSPECTIVE = """
            SELECT 
                COUNT(*) 
            FROM (
                SELECT 
                    es.cas, 
                    es.name, 
                    es.chemical_name, 
                    es.molecular_formula,
                    es.alternative_formula,
                    ec.name AS compartment_name, 
                    MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist, 
                    MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist, 
                    MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian
                FROM emission_substance sc 
                JOIN substance es ON sc.substance_id = es.id AND es.status = true 
                LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true 
                JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true 
                JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true 
                JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true 
                JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true 
                WHERE sc.status = true 
                GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name
            ) AS subquery;
            """;

    public static final String FIND_MIDPOINT_SUBSTANCE_FACTORS =
            "SELECT \n" +
                    "sc.id,\n" +
                    "es.cas,\n" +
                    "es.name,\n" +
                    "es.chemical_name,\n" +
                    "es.molecular_formula,\n" +
                    "es.alternative_formula,\n" +
                    "ec.name AS compartment_name,\n" +
                    "MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist,\n" +
                    "MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist,\n" +
                    "MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian \n" +
                    "FROM emission_substance sc \n" +
                    "JOIN substance es ON sc.substance_id = es.id AND es.status = true \n" +
                    "LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true \n" +
                    "JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true \n" +
                    "JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true \n" +
                    "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true \n" +
                    "JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true \n" +
                    "WHERE sc.status = true AND sc.id = :id \n" +
                    "GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name \n" +
                    "ORDER BY es.name ASC;";

    public static final String FIND_FACTOR_BY_KEYWORD =
            "SELECT sc.id, \n" +
                    "       es.cas, \n" +
                    "       es.name, \n" +
                    "       es.chemical_name, \n" +
                    "       es.molecular_formula, \n" +
                    "       es.alternative_formula, \n" +
                    "       ec.name AS compartment_name, \n" +
                    "       MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist, \n" +
                    "       MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist, \n" +
                    "       MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian \n" +
                    "FROM emission_substance sc \n" +
                    "JOIN substance es ON sc.substance_id = es.id AND es.status = true \n" +
                    "LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true \n" +
                    "JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true \n" +
                    "JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true \n" +
                    "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true \n" +
                    "JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true \n" +
                    "WHERE sc.status = true \n" +
                    "      AND (UPPER(es.name) LIKE UPPER(CONCAT('%', :keyword, '%'))\n" +
                    "           OR UPPER(es.chemical_name) LIKE UPPER(CONCAT('%', :keyword, '%'))\n" +
                    "           OR UPPER(es.molecular_formula) LIKE UPPER(CONCAT('%', :keyword, '%'))\n" +
                    "           OR UPPER(es.alternative_formula) LIKE UPPER(CONCAT('%', :keyword, '%'))) \n" +
                    "GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name \n" +
                    "ORDER BY es.name ASC;";

    public static final String FIND_FACTOR_BY_COMPARTMENT =
            "SELECT sc.id, \n" +
                    "       es.cas, \n" +
                    "       es.name, \n" +
                    "       es.chemical_name, \n" +
                    "       es.molecular_formula, \n" +
                    "       es.alternative_formula, \n" +
                    "       ec.name AS compartment_name, \n" +
                    "       MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist, \n" +
                    "       MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist, \n" +
                    "       MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian \n" +
                    "FROM emission_substance sc \n" +
                    "JOIN substance es ON sc.substance_id = es.id AND es.status = true \n" +
                    "LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true \n" +
                    "JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true \n" +
                    "JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true \n" +
                    "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true \n" +
                    "JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true \n" +
                    "WHERE sc.status = true \n" +
                    "\t  AND sc.emission_compartment_id = :compartmentId\n" +
                    "GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name \n" +
                    "ORDER BY es.name ASC;";

    public static final String FIND_FACTOR_BY_KEYWORD_AND_COMPARTMENT =
            "SELECT sc.id, \n" +
                    "       es.cas, \n" +
                    "       es.name, \n" +
                    "       es.chemical_name, \n" +
                    "       es.molecular_formula, \n" +
                    "       es.alternative_formula, \n" +
                    "       ec.name AS compartment_name, \n" +
                    "       MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist, \n" +
                    "       MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist, \n" +
                    "       MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian \n" +
                    "FROM emission_substance sc \n" +
                    "JOIN substance es ON sc.substance_id = es.id AND es.status = true \n" +
                    "LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true \n" +
                    "JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true \n" +
                    "JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true \n" +
                    "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true \n" +
                    "JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true \n" +
                    "WHERE sc.status = true \n" +
                    "      AND (UPPER(es.name) LIKE UPPER(CONCAT('%', :keyword, '%'))\n" +
                    "           OR UPPER(es.chemical_name) LIKE UPPER(CONCAT('%', :keyword, '%'))\n" +
                    "           OR UPPER(es.molecular_formula) LIKE UPPER(CONCAT('%', :keyword, '%'))\n" +
                    "           OR UPPER(es.alternative_formula) LIKE UPPER(CONCAT('%', :keyword, '%'))) \n" +
                    "\t  AND sc.emission_compartment_id = :compartmentId\n" +
                    "GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name \n" +
                    "ORDER BY es.name ASC;";

    public static final String COUNT_FACTORS_BY_KEYWORD = """
            SELECT 
                COUNT(*) 
            FROM (
                SELECT 
                    es.cas, 
                    es.name, 
                    es.chemical_name, 
                    es.molecular_formula,
                    es.alternative_formula,
                    ec.name AS compartment_name, 
                    MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist, 
                    MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist, 
                    MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian
                FROM emission_substance sc 
                JOIN substance es ON sc.substance_id = es.id AND es.status = true 
                LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true 
                JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true 
                JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true 
                JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true 
                JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true 
                WHERE sc.status = true 
                    AND (UPPER(es.name) LIKE UPPER(CONCAT('%', :keyword, '%'))
                        OR UPPER(es.chemical_name) LIKE UPPER(CONCAT('%', :keyword, '%'))
                        OR UPPER(es.molecular_formula) LIKE UPPER(CONCAT('%', :keyword, '%'))
                        OR UPPER(es.alternative_formula) LIKE UPPER(CONCAT('%', :keyword, '%')))\s
                GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name
            ) AS subquery;
            """;

    public static final String COUNT_FACTORS_BY_COMPARTMENT = """
            SELECT 
                COUNT(*) 
            FROM (
                SELECT 
                    es.cas, 
                    es.name, 
                    es.chemical_name, 
                    es.molecular_formula,
                    es.alternative_formula,
                    ec.name AS compartment_name, 
                    MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist, 
                    MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist, 
                    MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian
                FROM emission_substance sc 
                JOIN substance es ON sc.substance_id = es.id AND es.status = true 
                LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true 
                JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true 
                JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true 
                JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true 
                JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true 
                WHERE sc.status = true 
                    AND sc.emission_compartment_id = :compartmentId
                GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name
            ) AS subquery;
            """;

    public static final String COUNT_FACTORS_BY_KEYWORD_AND_COMPARTMENT = """
            SELECT 
                COUNT(*) 
            FROM (
                SELECT 
                    es.cas, 
                    es.name, 
                    es.chemical_name, 
                    es.molecular_formula,
                    es.alternative_formula,
                    ec.name AS compartment_name, 
                    MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist, 
                    MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist, 
                    MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian
                FROM emission_substance sc 
                JOIN substance es ON sc.substance_id = es.id AND es.status = true 
                LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true 
                JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true 
                JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true 
                JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true 
                JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true 
                WHERE sc.status = true 
                   AND (UPPER(es.name) LIKE UPPER(CONCAT('%', :keyword, '%'))
                        OR UPPER(es.chemical_name) LIKE UPPER(CONCAT('%', :keyword, '%'))
                        OR UPPER(es.molecular_formula) LIKE UPPER(CONCAT('%', :keyword, '%'))
                        OR UPPER(es.alternative_formula) LIKE UPPER(CONCAT('%', :keyword, '%')))\s
                            	  AND sc.emission_compartment_id = :compartmentId
                GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name
            ) AS subquery;
            """;
    public static final String CONNECTOR_EXIST_BY_START_END_PROCESS = "select (count(c) > 0) from Connector c " +
            "where ((c.startProcess.id = ?1 and c.endProcess.id = ?2) or (c.startProcess.id = ?2 and c.endProcess.id = ?1)) " +
            "and c.endProcess.status = true and c.startProcess.status = true and c.status = true";

    public static final String FIND_MIDPOINT_SUBSTANCE_FACTORS_TO_EXPORT =
            "SELECT \n" +
                    "sc.id,\n" +
                    "es.cas,\n" +
                    "es.name,\n" +
                    "es.chemical_name,\n" +
                    "es.molecular_formula,\n" +
                    "es.alternative_formula,\n" +
                    "ec.name AS compartment_name,\n" +
                    "MAX(CASE WHEN p.name = 'Individualist' THEN f.decimal_value END) AS individualist,\n" +
                    "MAX(CASE WHEN p.name = 'Hierarchist' THEN f.decimal_value END) AS hierarchist,\n" +
                    "MAX(CASE WHEN p.name = 'Egalitarian' THEN f.decimal_value END) AS egalitarian \n" +
                    "FROM emission_substance sc \n" +
                    "JOIN substance es ON sc.substance_id = es.id AND es.status = true \n" +
                    "LEFT JOIN emission_compartment ec ON sc.emission_compartment_id = ec.id AND ec.status = true \n" +
                    "JOIN midpoint_impact_characterization_factors f ON sc.id = f.emission_substance_id AND f.status = true \n" +
                    "JOIN impact_method_category imc ON imc.id = f.impact_method_category_id AND imc.status = true \n" +
                    "JOIN life_cycle_impact_assessment_method lciam ON lciam.id = imc.life_cycle_impact_assessment_method_id AND lciam.status = true \n" +
                    "JOIN perspective p ON p.id = lciam.perspective_id AND p.status = true \n" +
                    "WHERE sc.status = true AND imc.id = :methodCategoryId \n" +
                    "GROUP BY sc.id, es.cas, es.name, es.chemical_name, es.molecular_formula, es.alternative_formula, ec.name \n" +
                    "ORDER BY es.name ASC;";
}

