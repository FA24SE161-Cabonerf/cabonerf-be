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
    public static final String BAN_UNBAN_USER = "/ban-unban-user/{userId}";


    /************************
     *      IMPACTS APIs     *
     ************************/

    public static final String IMPACT = "/impacts";
    public static final String GET_ALL_IMPACT_CATEGORIES = "/impact-categories";
    public static final String GET_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";
    public static final String CREATE_IMPACT_CATEGORY = "/impact-categories";
    public static final String ADD_IMPACT_CATEGORY_TO_IMPACT_METHOD = "/impact-methods/{methodId}/impact-categories/{categoryId}";
    public static final String GET_CATEGORY_BY_METHOD_ID = "/impact-methods/{methodId}/impact-categories";
    public static final String GET_ALL_METHOD_NAME = "/impact-methods/name";
    public static final String UPDATE_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";
    public static final String DELETE_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";
    public static final String GET_METHOD_BY_IMPACT_CATEGORY_ID = "/impact-categories/{categoryId}/impact-methods";
    public static final String DELETE_IMPACT_CATEGORY_IN_METHOD = "/impact-categories/{categoryId}/impact-methods/{methodId}";

    public static final String GET_ALL_IMPACT_METHODS = "/impact-methods";
    public static final String GET_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";
    public static final String CREATE_IMPACT_METHOD = "/impact-methods";
    public static final String UPDATE_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";
    public static final String DELETE_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";


    public static final String GET_ALL_MIDPOINT_FACTORS = "/midpoint-factors";
    public static final String GET_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/{id}";
    public static final String DELETE_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/{id}";
    public static final String IMPORT_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/import";
    public static final String DOWNLOAD_ERROR_LOG_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/download";
    public static final String DOWNLOAD_TEMPLATE_MIDPOINT_FACTOR = "/midpoint-factors/factor-template";
    public static final String EXPORT_MIDPOINT_FACTOR = "/midpoint-factors/export";

    public static final String GET_ALL_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories";
    public static final String GET_MIDPOINT_IMPACT_CATEGORY_BY_ID = "/midpoint-categories";
    public static final String CREATE_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories";
    public static final String UPDATE_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories/{id}";
    public static final String DELETE_MIDPOINT_IMPACT_CATEGORY= "/midpoint-categories/{id}";



    /************************
     *      PROJECTS APIs     *
     ************************/

    public static final String PROJECT = "/projects";
    public static final String GET_PROJECT_BY_ID = "/{projectId}/{workspaceId}";
    public static final String UPDATE_DETAIL_PROJECT_BY_ID = "/{projectId}";
    public static final String DELETE_PROJECT = "/{projectId}";
    public static final String CHANGE_PROJECT_METHOD = "/{projectId}/impact-methods/{methodId}";
    public static final String CALCULATION_PROJECT = "/calculation/{projectId}";
    public static final String EXPORT_PROJECT = "/export/{projectId}";
    public static final String INTENSITY_PROJECT = "/intensity/{projectId}";

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
    public static final String LIFE_CYCLE_STAGE_UPDATE = "/{id}";
    public static final String LIFE_CYCLE_STAGE_DELETE = "/{id}";

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

    /************************
     *   EXCHANGE APIs   *
     ************************/
    public static final String EXCHANGE = "/exchanges";
    public static final String ELEMENTARY_EXCHANGE = "/elementary-exchange";
    public static final String PRODUCT_EXCHANGE = "/product-exchange";
    public static final String REMOVE_EXCHANGE = "/{exchangeId}";
    public static final String UPDATE_EXCHANGE = "/{exchangeId}";

    /************************
     *   CONNECTORS APIs   *
     ************************/
    public static final String CONNECTOR = "/connectors";
    public static final String DELETE_CONNECTOR = "/{id}";


    /************************
     *   ORGANIZATIONS APIs   *
     ************************/
    public static final String ORGANIZATION = "/organizations";
    public static final String UPDATE_ORGANIZATION = "/{organizationId}";
    public static final String DELETE_ORGANIZATION = "/{organizationId}";

    public static final String MANAGER = "/manager";

    /************************
     *   EMISSIONS COMPARTMENT APIs   *
     ************************/
    public static final String UPDATE_EMISSION_COMPARTMENT = "/{emissionCompartmentId}";
    public static final String DELETE_EMISSION_COMPARTMENT = "/{emissionCompartmentId}";


}
