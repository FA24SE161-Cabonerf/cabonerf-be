package com.example.cabonerfbe.enums;

public class MessageConstants {
    public static final String EMAIL_PASSWORD_WRONG = "Email or password wrong!";
    public static final String EMAIL_ALREADY_EXIST = "Email already exist.";
    public static final String CONFIRM_PASSWORD_NOT_MATCH = "Confirm Passwords do not match.";
    public static final String USER_NOT_FOUND = "User not found!";
    public static final String INVALID_REFRESH_TOKEN = "Invalid or expired refresh token";
    public static final String INVALID_ACCESS_TOKEN = "Invalid or expired access token";
    public static final String INVALID_VERIFICATION_CODE = "Invalid verification code";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String GET_ALL_IMPACT_METHODS_SUCCESS = "Get all impact methods success";
    public static final String GET_ALL_IMPACT_CATEGORIES_SUCCESS = "Get all impact categories success";
    public static final String GET_IMPACT_CATEGORY_BY_ID_SUCCESS = "Get impact category by id success";
    public static final String NO_IMPACT_CATEGORY_FOUND = "No impact category found";
    public static final String GET_IMPACT_METHOD_BY_ID_SUCCESS = "Get impact method by id success";
    public static final String NO_IMPACT_METHOD_FOUND = "No impact method found";

    public static final String GET_CATEGORY_BY_METHOD_ID_SUCCESS = "Get category by method id success";

    public static final String NO_MIDPOINT_IMPACT_CHARACTERIZATION_FACTOR = "No midpoint impact characterization factor found";
    public static final String GET_ALL_MIDPOINT_FACTORS_SUCCESS = "Get all midpoint impact characterization factors success";
    public static final String GET_ALL_MIDPOINT_SUBSTANCE_FACTORS_SUCCESS = "Get all midpoint substance factors with perspectives for admin success";
    public static final String GET_MIDPOINT_FACTOR_BY_ID_SUCCESS = "Get midpoint impact factor by id success";
    public static final String GET_PROJECT_LIST_SUCCESS = "Get project list success";
    public static final String GET_PROJECT_BY_ID_SUCCESS = "Get project by id success";

    public static final String CURRENT_PAGE_EXCEED_TOTAL_PAGES = "Current page exceeds total pages.";
    public static final String GET_ALL_UNITS_SUCCESS = "Get all units success";
    public static final String NO_UNIT_FOUND = "No unit found";
    public static final String ADD_UNIT_TO_UNIT_GROUP_SUCCESS = "Add unit success";
    public static final String GET_UNIT_BY_ID_SUCCESS = "Get unit by id success";
    public static final String UPDATE_UNIT_BY_ID_SUCCESS = "Update unit by id success";
    public static final String DELETE_UNIT_BY_ID_SUCCESS = "Delete unit by id success";
    public static final String DEFAULT_UNIT_EXIST = "Default unit exist. Please check isDefault field";

    public static final String NO_UNIT_GROUP_FOUND = "No unit group found";

    public static final String GET_ALL_UNIT_GROUP_SUCCESS = "Get all unit group success";
    public static final String GET_UNIT_GROUP_BY_ID_SUCCESS = "Get unit group by success";
    public static final String GET_UNIT_GROUP_BY_ID_NOT_FOUND = "Unit group not exist";

    public static final String GET_ALL_LIFE_STAGE_SUCCESS = "Get all life cycle stage success";
    public static final String GET_LIFE_STAGE_BY_ID_SUCCESS = "Get life cycle stage by success";
    public static final String GET_LIFE_STAGE_BY_ID_NOT_FOUND = "Life cycle stage not exist";
    public static final String CREATE_UNIT_GROUP_SUCCESS = "Create unit group success";
    public static final String UNIT_GROUP_EXIST = "Unit group exist";
    public static final String UNIT_GROUP_NAME_EXIST = "Unit group name exist";
    public static final String UPDATE_UNIT_GROUP_BY_ID_SUCCESS = "Update unit group by id success";
    public static final String DELETE_UNIT_GROUP_BY_ID_SUCCESS = "Delete unit group by id success";

    public static final String CREATE_IMPACT_METHOD_SUCCESS = "Create impact method success";
    public static final String IMPACT_METHOD_EXIST = "Impact method exist";
    public static final String NO_PERSPECTIVE_FOUND = "No perspective not exist";
    public static final String UPDATE_IMPACT_METHOD_SUCCESS = "Update impact method by id success";
    public static final String DELETE_IMPACT_METHOD_SUCCESS = "Delete impact method by id success";
    public static final String CREATE_IMPACT_CATEGORY_SUCCESS = "Create impact category success";
    public static final String GET_ALL_PERSPECTIVE_SUCCESS = "Get all perspective success";
    public static final String UPDATE_PERSPECTIVE_SUCCESS = "Update perspective success";
    public static final String GET_ALL_EMISSION_COMPARTMENTS_SUCCESS = "Get all emission compartments success";
    public static final String NO_EMISSION_COMPARTMENT_FOUND = "No emission compartment found";
    public static final String GET_ALL_MIDPOINT_IMPACT_CATEGORY_SUCCESS = "Get all midpoint impact categories success";

    public static final String NO_MIDPOINT_IMPACT_CATEGORY_FOUND = "No midpoint impact category found";
    public static final String ADD_IMPACT_CATEGORY_TO_IMPACT_METHOD_SUCCESS = "Add impact category to impact method success";
    public static final String IMPACT_CATEGORY_ALREADY_IN_METHOD = "The impact category is already in the method";
    public static final String UPDATE_IMPACT_CATEGORY_SUCCESS = "Update impact category by id success";
    public static final String DELETE_IMPACT_CATEGORY_SUCCESS = "Delete impact category by id success";
    public static final String GET_METHOD_BY_IMPACT_CATEGORY_SUCCESS = "Get method by impact category id success";
    public static final String DELETE_CATEGORY_FROM_METHOD_SUCCESS = "Delete impact category from method success";
    public static final String NO_LIFE_CYCLE_STAGE_FOUND = "Life cycle stage not exist";
    public static final String NO_PROJECT_FOUND = "Project not exist";
    public static final String NO_PROCESS_FOUND = "Process not exist";
    public static final String NO_ELEMENTARY_FLOW_FOUND = "Elementary flow not exist";

    public static final String START_END_PROCESS_SAME_ERROR = "Start and end process IDs must be different";
    public static final String CONNECTOR_ALREADY_EXIST = "Connector between the two processes exist";
    public static final String NOT_VALID_PROCESS_CONNECTOR = "Either the start or end process does not exist";
    public static final String PROCESS_IN_DIFFERENT_PROJECTS = "Start and end processes must belong to the same project";

    public static final String NO_EXCHANGE_FOUND = "Exchange not exist";
    public static final String INVALID_EXCHANGE = "Invalid exchange id or type";
    public static final String EXCHANGE_UNIT_GROUP_DIFFERENT = "Unit group of the 2 exchanges must be the same";
    public static final String EXCHANGE_AND_PROCESS_DIFFERENT = "Exchange does not belong to given process";
    public static final String EXCHANGE_NAME_DIFFERENT = "Name of the 2 exchanges must be the same";

    public static final String ELEMENTARY_EXIST = "Elementary already exists";
    public static final String PRODUCT_EXIST = "Output product already exists in process";
    public static final String REMOVE_EXCHANGE_SUCCESS = "Remove exchange success";
    public static final String CREATE_PRODUCT_EXCHANGE_SUCCESS = "Create product exchanges success";
    public static final String CREATE_ELEMENTARY_EXCHANGE_SUCCESS = "Create elementary exchanges success";

    public static final String UPDATE_EXCHANGE_SUCCESS = "Update exchange success";

    public static final String ERROR_CREATING_PROCESS_IMPACT_VALUE = "Error creating process impact value";
    public static final String NO_PROCESS_IN_PROJECT = "There must be at least one process to calculate";
}
