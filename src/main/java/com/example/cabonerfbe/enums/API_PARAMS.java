package com.example.cabonerfbe.enums;

/**
 * The class Api params.
 *
 * @author SonPHH.
 */
public class API_PARAMS {
    /**
     * The constant API_VERSION.
     */
    public static final String API_VERSION = "/api/v1";

    /**
     * The constant USERS.
     */
    public static final String USERS = "/users";
    /**
     * The constant LOGIN_BY_EMAIL.
     */
    public static final String LOGIN_BY_EMAIL = "/login";
    /**
     * The constant REGISTER.
     */
    public static final String REGISTER = "/register";
    /**
     * The constant REFRESH_TOKEN.
     */
    public static final String REFRESH_TOKEN = "/refresh";
    /**
     * The constant ACTIVE_USER.
     */
    public static final String ACTIVE_USER = "/active-user";
    /**
     * The constant LOGOUT.
     */
    public static final String LOGOUT = "/logout";
    /**
     * The constant ME.
     */
    public static final String ME = "/me";
    /**
     * The constant EMAIL_VERIFY.
     */
    public static final String EMAIL_VERIFY = "/email-verify";
    /**
     * The constant MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR.
     */
    public static final String MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR = "/midpoint-impact-characterization-factors";
    /**
     * The constant IMPORT_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR.
     */
    public static final String IMPORT_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR = "/import";
    /**
     * The constant BAN_UNBAN_USER.
     */
    public static final String BAN_UNBAN_USER = "/ban-unban-user/{userId}";
    /**
     * The constant UPDATE_AVATAR_USER.
     */
    public static final String UPDATE_AVATAR_USER = "/avatar";
    /**
     * The constant GET_USER_TO_INVITE.
     */
    public static final String GET_USER_TO_INVITE = "/invite";
    /**
     * The constant GET_USER_TO_DASHBOARD.
     */
    public static final String GET_USER_TO_DASHBOARD = "/count-user-new";
    /**
     * The constant GET_ALL_USER_TO_DASHBOARD.
     */
    public static final String GET_ALL_USER_TO_DASHBOARD = "/count-all-user";
    /**
     * The constant CHANGE_PASSWORD.
     */
    public static final String CHANGE_PASSWORD = "/password";
    /**
     * The constant UPDATE_PROFILE.
     */
    public static final String UPDATE_PROFILE = "/profile";

    /**
     * The constant IMPACT.
     */
    public static final String IMPACT = "/impacts";
    /**
     * The constant GET_ALL_IMPACT_CATEGORIES.
     */
    public static final String GET_ALL_IMPACT_CATEGORIES = "/impact-categories";
    /**
     * The constant GET_IMPACT_CATEGORY_BY_ID.
     */
    public static final String GET_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";
    /**
     * The constant CREATE_IMPACT_CATEGORY.
     */
    public static final String CREATE_IMPACT_CATEGORY = "/impact-categories";
    /**
     * The constant ADD_IMPACT_CATEGORY_TO_IMPACT_METHOD.
     */
    public static final String ADD_IMPACT_CATEGORY_TO_IMPACT_METHOD = "/impact-methods/{methodId}/impact-categories/{categoryId}";
    /**
     * The constant GET_CATEGORY_BY_METHOD_ID.
     */
    public static final String GET_CATEGORY_BY_METHOD_ID = "/impact-methods/{methodId}/impact-categories";
    /**
     * The constant GET_ALL_METHOD_NAME.
     */
    public static final String GET_ALL_METHOD_NAME = "/impact-methods/name";
    /**
     * The constant UPDATE_IMPACT_CATEGORY_BY_ID.
     */
    public static final String UPDATE_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";
    /**
     * The constant DELETE_IMPACT_CATEGORY_BY_ID.
     */
    public static final String DELETE_IMPACT_CATEGORY_BY_ID = "/impact-categories/{categoryId}";
    /**
     * The constant GET_METHOD_BY_IMPACT_CATEGORY_ID.
     */
    public static final String GET_METHOD_BY_IMPACT_CATEGORY_ID = "/impact-categories/{categoryId}/impact-methods";
    /**
     * The constant DELETE_IMPACT_CATEGORY_IN_METHOD.
     */
    public static final String DELETE_IMPACT_CATEGORY_IN_METHOD = "/impact-categories/{categoryId}/impact-methods/{methodId}";

    /**
     * The constant GET_ALL_IMPACT_METHODS.
     */
    public static final String GET_ALL_IMPACT_METHODS = "/impact-methods";
    /**
     * The constant GET_IMPACT_METHOD_BY_ID.
     */
    public static final String GET_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";
    /**
     * The constant CREATE_IMPACT_METHOD.
     */
    public static final String CREATE_IMPACT_METHOD = "/impact-methods";
    /**
     * The constant UPDATE_IMPACT_METHOD_BY_ID.
     */
    public static final String UPDATE_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";
    /**
     * The constant DELETE_IMPACT_METHOD_BY_ID.
     */
    public static final String DELETE_IMPACT_METHOD_BY_ID = "/impact-methods/{methodId}";


    /**
     * The constant GET_ALL_MIDPOINT_FACTORS.
     */
    public static final String GET_ALL_MIDPOINT_FACTORS = "/midpoint-factors";
    /**
     * The constant GET_MIDPOINT_FACTOR_BY_ID.
     */
    public static final String GET_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/{id}";
    /**
     * The constant DELETE_MIDPOINT_FACTOR_BY_ID.
     */
    public static final String DELETE_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/{id}";
    /**
     * The constant IMPORT_MIDPOINT_FACTOR_BY_ID.
     */
    public static final String IMPORT_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/import";
    /**
     * The constant DOWNLOAD_ERROR_LOG_MIDPOINT_FACTOR_BY_ID.
     */
    public static final String DOWNLOAD_ERROR_LOG_MIDPOINT_FACTOR_BY_ID = "/midpoint-factors/download";
    /**
     * The constant DOWNLOAD_TEMPLATE_MIDPOINT_FACTOR.
     */
    public static final String DOWNLOAD_TEMPLATE_MIDPOINT_FACTOR = "/midpoint-factors/factor-template";
    /**
     * The constant EXPORT_MIDPOINT_FACTOR.
     */
    public static final String EXPORT_MIDPOINT_FACTOR = "/midpoint-factors/export";

    /**
     * The constant GET_ALL_MIDPOINT_IMPACT_CATEGORY.
     */
    public static final String GET_ALL_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories";
    /**
     * The constant GET_MIDPOINT_IMPACT_CATEGORY_BY_ID.
     */
    public static final String GET_MIDPOINT_IMPACT_CATEGORY_BY_ID = "/midpoint-categories";
    /**
     * The constant CREATE_MIDPOINT_IMPACT_CATEGORY.
     */
    public static final String CREATE_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories";
    /**
     * The constant UPDATE_MIDPOINT_IMPACT_CATEGORY.
     */
    public static final String UPDATE_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories/{id}";
    /**
     * The constant DELETE_MIDPOINT_IMPACT_CATEGORY.
     */
    public static final String DELETE_MIDPOINT_IMPACT_CATEGORY = "/midpoint-categories/{id}";


    /**
     * The constant PROJECT.
     */
    public static final String PROJECT = "/projects";
    /**
     * The constant GET_PROJECT_BY_ID.
     */
    public static final String GET_PROJECT_BY_ID = "/{projectId}";
    /**
     * The constant UPDATE_DETAIL_PROJECT_BY_ID.
     */
    public static final String UPDATE_DETAIL_PROJECT_BY_ID = "/{projectId}";
    /**
     * The constant DELETE_PROJECT.
     */
    public static final String DELETE_PROJECT = "/{projectId}";
    /**
     * The constant CHANGE_PROJECT_METHOD.
     */
    public static final String CHANGE_PROJECT_METHOD = "/{projectId}/impact-methods/{methodId}";
    /**
     * The constant CALCULATION_PROJECT.
     */
    public static final String CALCULATION_PROJECT = "/calculation";
    /**
     * The constant EXPORT_PROJECT.
     */
    public static final String EXPORT_PROJECT = "/{projectId}/export";
    /**
     * The constant INTENSITY_PROJECT.
     */
    public static final String INTENSITY_PROJECT = "/{projectId}/intensity";
    /**
     * The constant COUNT_PROJECT.
     */
    public static final String COUNT_PROJECT = "/count-project";
    /**
     * The constant SUM_IMPACT.
     */
    public static final String SUM_IMPACT = "/sum-impact";
    /**
     * The constant SET_FAVORITE_PROJECT.
     */
    public static final String SET_FAVORITE_PROJECT = "/{projectId}/favorite";

    /**
     * The constant PROCESS.
     */
    public static final String PROCESS = "/process";
    /**
     * The constant PROCESS_BY_ID.
     */
    public static final String PROCESS_BY_ID = "/{id}";

    /**
     * The constant GET_ALL_UNITS.
     */
    public static final String GET_ALL_UNITS = "/units";
    /**
     * The constant GET_UNIT_BY_ID.
     */
    public static final String GET_UNIT_BY_ID = "/units/{unitId}";
    /**
     * The constant GET_ALL_UNITS_FROM_UNIT_GROUP_ID.
     */
    public static final String GET_ALL_UNITS_FROM_UNIT_GROUP_ID = "/unit-groups/{groupId}/units";
    /**
     * The constant UPDATE_UNIT_BY_ID.
     */
    public static final String UPDATE_UNIT_BY_ID = "/units/{unitId}";
    /**
     * The constant DELETE_UNIT_BY_ID.
     */
    public static final String DELETE_UNIT_BY_ID = "/units/{unitId}";
    /**
     * The constant ADD_UNIT_TO_UNIT_GROUP.
     */
    public static final String ADD_UNIT_TO_UNIT_GROUP = "/unit-groups/{groupId}/units";

    /**
     * The constant GET_ALL_UNIT_GROUPS.
     */
    public static final String GET_ALL_UNIT_GROUPS = "/unit-group";


    /**
     * The constant UNIT_GROUP.
     */
    public static final String UNIT_GROUP = "/unit-groups";
    /**
     * The constant UNIT_GROUP_BY_ID.
     */
    public static final String UNIT_GROUP_BY_ID = "/{id}";
    /**
     * The constant CREATE_UNIT_GROUP.
     */
    public static final String CREATE_UNIT_GROUP = "/";
    /**
     * The constant UPDATE_UNIT_GROUP_BY_ID.
     */
    public static final String UPDATE_UNIT_GROUP_BY_ID = "/{groupId}";
    /**
     * The constant DELETE_UNIT_GROUP_BY_ID.
     */
    public static final String DELETE_UNIT_GROUP_BY_ID = "/{groupId}";


    /**
     * The constant LIFE_STAGE.
     */
    public static final String LIFE_STAGE = "/life-cycle-stages";
    /**
     * The constant LIFE_STAGE_BY_ID.
     */
    public static final String LIFE_STAGE_BY_ID = "/{id}";
    /**
     * The constant LIFE_CYCLE_STAGE_UPDATE.
     */
    public static final String LIFE_CYCLE_STAGE_UPDATE = "/{id}";
    /**
     * The constant LIFE_CYCLE_STAGE_DELETE.
     */
    public static final String LIFE_CYCLE_STAGE_DELETE = "/{id}";

    /**
     * The constant UNIT.
     */
    public static final String UNIT = "/units";
    /**
     * The constant UNIT_BY_ID.
     */
    public static final String UNIT_BY_ID = "/{id}";

    /**
     * The constant ADMIN.
     */
    public static final String ADMIN = "/admin";

    /**
     * The constant PERSPECTIVE.
     */
    public static final String PERSPECTIVE = "/perspectives";
    /**
     * The constant GET_ALL_PERSPECTIVE.
     */
    public static final String GET_ALL_PERSPECTIVE = "/";
    /**
     * The constant GET_BY_ID_PERSPECTIVE.
     */
    public static final String GET_BY_ID_PERSPECTIVE = "/{id}";

    /**
     * The constant EMISSIONS.
     */
    public static final String EMISSIONS = "/emissions";
    /**
     * The constant GET_ALL_EMISSION_COMPARTMENTS.
     */
    public static final String GET_ALL_EMISSION_COMPARTMENTS = "/emission-compartments";
    /**
     * The constant GET_EMISSION_COMPARTMENTS_BY_ID.
     */
    public static final String GET_EMISSION_COMPARTMENTS_BY_ID = "/emission-compartments/{id}";
    /**
     * The constant EMISSION_SUBSTANCE.
     */
    public static final String EMISSION_SUBSTANCE = "/emission-substance";
    /**
     * The constant EMISSION_SUBSTANCE_COUNT.
     */
    public static final String EMISSION_SUBSTANCE_COUNT = "/count-emission-substance";

    /**
     * The constant EXCHANGE.
     */
    public static final String EXCHANGE = "/exchanges";
    /**
     * The constant ELEMENTARY_EXCHANGE.
     */
    public static final String ELEMENTARY_EXCHANGE = "/elementary-exchange";
    /**
     * The constant PRODUCT_EXCHANGE.
     */
    public static final String PRODUCT_EXCHANGE = "/product-exchange";
    /**
     * The constant REMOVE_EXCHANGE.
     */
    public static final String REMOVE_EXCHANGE = "/{exchangeId}";
    /**
     * The constant UPDATE_EXCHANGE.
     */
    public static final String UPDATE_EXCHANGE = "/{exchangeId}";

    /**
     * The constant CONNECTOR.
     */
    public static final String CONNECTOR = "/connectors";
    /**
     * The constant DELETE_CONNECTOR.
     */
    public static final String DELETE_CONNECTOR = "/{id}";


    /**
     * The constant ORGANIZATION.
     */
    public static final String ORGANIZATION = "/organizations";
    /**
     * The constant UPDATE_ORGANIZATION.
     */
    public static final String UPDATE_ORGANIZATION = "/{organizationId}";
    /**
     * The constant DELETE_ORGANIZATION.
     */
    public static final String DELETE_ORGANIZATION = "/{organizationId}";
    /**
     * The constant CONFIRM_CREATE_ORGANIZATION.
     */
    public static final String CONFIRM_CREATE_ORGANIZATION = "/confirm";
    /**
     * The constant INVITE_MEMBER_ORGANIZATION.
     */
    public static final String INVITE_MEMBER_ORGANIZATION = "/invite";
    /**
     * The constant ACCEPT_INVITE_ORGANIZATION.
     */
    public static final String ACCEPT_INVITE_ORGANIZATION = "/accept";
    /**
     * The constant DENY_INVITE_ORGANIZATION.
     */
    public static final String DENY_INVITE_ORGANIZATION = "/deny";
    /**
     * The constant GET_MEMBER_IN_ORGANIZATION.
     */
    public static final String GET_MEMBER_IN_ORGANIZATION = "/{organizationId}/members";
    /**
     * The constant REMOVE_MEMBER_IN_ORGANIZATION.
     */
    public static final String REMOVE_MEMBER_IN_ORGANIZATION = "/remove-member/{userOrganizationId}";
    /**
     * The constant GET_LIST_INVITE_BY_USER.
     */
    public static final String GET_LIST_INVITE_BY_USER = "/invite-list";
    /**
     * The constant UPLOAD_LOGO.
     */
    public static final String UPLOAD_LOGO = "/{organizationId}/upload-logo";
    /**
     * The constant GET_ORGANIZATION_BY_ID.
     */
    public static final String GET_ORGANIZATION_BY_ID = "/{organizationId}";
    /**
     * The constant OUT_ORGANIZATION.
     */
    public static final String OUT_ORGANIZATION = "/leave-organization/{userOrganizationId}";

    /**
     * The constant MANAGER.
     */
    public static final String MANAGER = "/manager";
    /**
     * The constant ORGANIZATION_MANAGER.
     */
    public static final String ORGANIZATION_MANAGER = "/organization-manager";

    /**
     * The constant WORKSPACE.
     */
    public static final String WORKSPACE = "/workspaces";

    /**
     * The constant CONTRACT.
     */
    public static final String CONTRACT = "/contracts";
    /**
     * The constant DOWNLOAD_CONTRACT.
     */
    public static final String DOWNLOAD_CONTRACT = "/{contractId}";

    /**
     * The constant UPDATE_EMISSION_COMPARTMENT.
     */
    public static final String UPDATE_EMISSION_COMPARTMENT = "/{emissionCompartmentId}";
    /**
     * The constant DELETE_EMISSION_COMPARTMENT.
     */
    public static final String DELETE_EMISSION_COMPARTMENT = "/{emissionCompartmentId}";

    /**
     * The constant OBJECT_LIBRARY.
     */
    public static final String OBJECT_LIBRARY = "/object-library";
    /**
     * The constant SEARCH_OBJECT_LIBRARY_OF_ORGANIZATION.
     */
    public static final String SEARCH_OBJECT_LIBRARY_OF_ORGANIZATION = "/organizations/{organizationId}";
    /**
     * The constant REMOVE_PROCESS_FROM_OBJECT_LIBRARY.
     */
    public static final String REMOVE_PROCESS_FROM_OBJECT_LIBRARY = "/organizations/{organizationId}";
    /**
     * The constant SAVE_PROCESS_TO_OBJECT_LIBRARY.
     */
    public static final String SAVE_PROCESS_TO_OBJECT_LIBRARY = "/{processId}";
    /**
     * The constant ADD_FROM_OBJECT_LIBRARY_TO_PROJECT.
     */
    public static final String ADD_FROM_OBJECT_LIBRARY_TO_PROJECT = "/{processId}/projects/{projectId}";

    /**
     * The constant INDUSTRY_CODE.
     */
    public static final String INDUSTRY_CODE = "/industry-code";
    /**
     * The constant UPDATE_INDUSTRY_CODE.
     */
    public static final String UPDATE_INDUSTRY_CODE = "/{industryCodeId}";
    /**
     * The constant DELETE_INDUSTRY_CODE.
     */
    public static final String DELETE_INDUSTRY_CODE = "/{industryCodeId}";
    /**
     * The constant GET_INDUSTRY_CODE_BY_ORGANIZATION.
     */
    public static final String GET_INDUSTRY_CODE_BY_ORGANIZATION = "/{organizationId}";
    /**
     * The constant GET_INDUSTRY_CODE_TO_CREATE.
     */
    public static final String GET_INDUSTRY_CODE_TO_CREATE = "/get-create";

    /**
     * The constant SYSTEM_BOUNDARY.
     */
    public static final String SYSTEM_BOUNDARY = "/system-boundary";

    /**
     * The constant DATASET.
     */
    public static final String DATASET = "/datasets";
}
