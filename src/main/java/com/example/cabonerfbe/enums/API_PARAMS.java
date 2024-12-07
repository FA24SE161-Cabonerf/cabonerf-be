package com.example.cabonerfbe.enums;

public class API_PARAMS {
    public static final String API_VERSION = "/api/v1";

    /************************
     *      USERS APIs     *
     ************************/
    public static final String USERS = "/users";
    public static final String LOGIN_BY_EMAIL = "/login";
    public static final String REGISTER = "/register";
    public static final String REFRESH_TOKEN = "/refresh";
    public static final String ACTIVE_USER = "/active-user";
    public static final String LOGOUT = "/logout";
    public static final String ME = "/me";
    public static final String EMAIL_VERIFY = "/email-verify";
    public static final String MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR = "/midpoint-impact-characterization-factors";
    public static final String IMPORT_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR = "/import";
    public static final String EXCHANGE = "/exchanges";
    public static final String CREATE_ELEMENTARY_EXCHANGE = "/create-elementary-exchange";
    public static final String CREATE_PRODUCT_EXCHANGE = "/create-product-exchange";
    public static final String CONNECTOR = "/connectors";


    /************************
     *      IMPACTS APIs     *
     ************************/

    public static final String IMPACT = "/impacts";
    public static final String GET_ALL_IMPACT_CATEGORIES = "/impact-categories";
    public static final String GET_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";
    public static final String CREATE_IMPACT_CATEGORY = "/impact-categories";
    public static final String ADD_IMPACT_CATEGORY_TO_IMPACT_METHOD = "/impact-methods/{methodId}/impact-categories/{categoryId}";
    public static final String GET_CATEGORY_BY_METHOD_ID = "/impact-methods/{methodId}/impact-categories";
    public static final String UPDATE_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";
    public static final String DELETE_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";


    public static final String GET_ALL_IMPACT_METHODS = "/impact-methods";
    public static final String GET_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";
    public static final String CREATE_IMPACT_METHOD = "/impact-methods";
    public static final String UPDATE_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";
    public static final String DELETE_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";


    public static final String GET_ALL_MIDPOINT_FACTORS = "/midpoint-factors";
    public static final String GET_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/{id}";

    public static final String GET_ALL_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories";
    public static final String GET_MIDPOINT_IMPACT_CATEGORY_BY_ID = "/midpoint-categories";



    /************************
     *      PROJECTS APIs     *
     ************************/

    public static final String PROJECT = "/projects";
    public static final String GET_PROJECT_BY_ID = "/{id}";
    public static final String UPDATE_DETAIL_PROJECT_BY_ID = "/{id}";
    public static final String DELETE_PROJECT = "/{id}";

    /************************
     *      PROCESS APIs     *
     ************************/
    public static final String PROCESS = "/process";
    public static final String PROCESS_BY_ID = "/{id}";

    /************************
     *      UNIT APIs     *
     ************************/
    public static final String GET_ALL_UNITS = "/units";
    public static final String GET_UNIT_BY_ID = "/units/{unitId}";
    public static final String GET_ALL_UNITS_FROM_UNIT_GROUP_ID = "/unit-groups/{groupId}/units";
    public static final String UPDATE_UNIT_BY_ID = "/units/{unitId}";
    public static final String DELETE_UNIT_BY_ID = "/units/{unitId}";
    public static final String ADD_UNIT_TO_UNIT_GROUP = "/unit-groups/{groupId}/units";

    public static final String GET_ALL_UNIT_GROUPS = "/unit-group";


    /************************
     *      UNIT GROUP APIs     *
     ************************/
    public static final String UNIT_GROUP = "/unit-groups";
    public static final String UNIT_GROUP_BY_ID = "/{id}";
    public static final String CREATE_UNIT_GROUP = "/";
    public static final String UPDATE_UNIT_GROUP_BY_ID = "/{groupId}";
    public static final String DELETE_UNIT_GROUP_BY_ID = "/{groupId}";


    /************************
     *      LIFE CYCLE STAGE APIs     *
     ************************/
    public static final String LIFE_STAGE = "/life-cycle-stages";
    public static final String LIFE_STAGE_BY_ID = "/{id}";

    /************************
     *      UNIT APIs     *
     ************************/
    public static final String UNIT = "/units";
    public static final String UNIT_BY_ID = "/{id}";

    /************************
     *      ADMIN APIs      *
     ************************/
    public static final String ADMIN = "/admin";

    /************************
     *   PERSPECTIVE APIs   *
     ************************/
    public static final String PERSPECTIVE = "/perspectives";
    public static final String GET_ALL_PERSPECTIVE = "/";
    public static final String GET_BY_ID_PERSPECTIVE = "/{id}";

    /************************
     *   EMISSIONS APIs   *
     ************************/
    public static final String EMISSIONS = "/emissions";
    public static final String GET_ALL_EMISSION_COMPARTMENTS = "/emission-compartments";
    public static final String GET_EMISSION_COMPARTMENTS_BY_ID = "/emission-compartments/{id}";
    public static final String EMISSION_SUBSTANCE = "/emission-substance";
}
