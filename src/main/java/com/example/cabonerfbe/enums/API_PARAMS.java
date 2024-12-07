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
    public static final String UPDATE_AVATAR_USER = "/avatar";
    public static final String GET_USER_TO_INVITE = "/invite";
    public static final String GET_USER_TO_DASHBOARD = "/count-user-new";
    public static final String GET_ALL_USER_TO_DASHBOARD = "/count-all-user";
    public static final String CHANGE_PASSWORD = "/password";
    public static final String UPDATE_PROFILE = "/profile";

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
    public static final String DELETE_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories/{id}";


    /************************
     *      PROJECTS APIs     *
     ************************/

    public static final String PROJECT = "/projects";
    public static final String GET_PROJECT_BY_ID = "/{projectId}";
    public static final String UPDATE_DETAIL_PROJECT_BY_ID = "/{projectId}";
    public static final String DELETE_PROJECT = "/{projectId}";
    public static final String CHANGE_PROJECT_METHOD = "/{projectId}/impact-methods/{methodId}";
    public static final String CALCULATION_PROJECT = "/calculation";
    public static final String EXPORT_PROJECT = "/{projectId}/export";
    public static final String INTENSITY_PROJECT = "/{projectId}/intensity";
    public static final String COUNT_PROJECT = "/count-project";
    public static final String SUM_IMPACT = "/sum-impact";
    public static final String SET_FAVORITE_PROJECT = "/{projectId}/favorite";

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
    public static final String EMISSION_SUBSTANCE_COUNT = "/count-emission-substance";

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
    public static final String CONFIRM_CREATE_ORGANIZATION = "/confirm";
    public static final String INVITE_MEMBER_ORGANIZATION = "/invite";
    public static final String ACCEPT_INVITE_ORGANIZATION = "/accept";
    public static final String DENY_INVITE_ORGANIZATION = "/deny";
    public static final String GET_MEMBER_IN_ORGANIZATION = "/{organizationId}/members";
    public static final String REMOVE_MEMBER_IN_ORGANIZATION = "/remove-member/{userOrganizationId}";
    public static final String GET_LIST_INVITE_BY_USER = "/invite-list";
    public static final String UPLOAD_LOGO = "/{organizationId}/upload-logo";
    public static final String GET_ORGANIZATION_BY_ID = "/{organizationId}";
    public static final String OUT_ORGANIZATION = "/leave-organization/{userOrganizationId}";

    public static final String MANAGER = "/manager";
    public static final String ORGANIZATION_MANAGER = "/organization-manager";

    /************************
     *   WORKSPACE APIs   *
     ************************/
    public static final String WORKSPACE = "/workspaces";

    /************************
     *   CONTRACT APIs *
     ************************/
    public static final String CONTRACT = "/contracts";
    public static final String DOWNLOAD_CONTRACT = "/{contractId}";

    /************************
     *   EMISSIONS COMPARTMENT APIs   *
     ************************/
    public static final String UPDATE_EMISSION_COMPARTMENT = "/{emissionCompartmentId}";
    public static final String DELETE_EMISSION_COMPARTMENT = "/{emissionCompartmentId}";

    /******************
     * OBJECT LIBRARY *
     *****************/
    public static final String OBJECT_LIBRARY = "/object-library";
    public static final String SEARCH_OBJECT_LIBRARY_OF_ORGANIZATION = "/organizations/{organizationId}";
    public static final String REMOVE_PROCESS_FROM_OBJECT_LIBRARY = "/{processId}";
    public static final String SAVE_PROCESS_TO_OBJECT_LIBRARY = "/{processId}";
    public static final String ADD_FROM_OBJECT_LIBRARY_TO_PROJECT = "/{processId}/projects/{projectId}";

    /************************
     *   INDUSTRY CODE APIs   *
     ************************/
    public static final String INDUSTRY_CODE = "/industry-code";
    public static final String UPDATE_INDUSTRY_CODE = "/{industryCodeId}";
    public static final String DELETE_INDUSTRY_CODE = "/{industryCodeId}";
    public static final String GET_INDUSTRY_CODE_BY_ORGANIZATION = "/{organizationId}";

}
