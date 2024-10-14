package com.example.caboneftbe.enums;

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
    public static final String PROCESS = "/process";
    public static final String EXCHANGE = "/exchanges";
    public static final String CREATE_ELEMENTARY_EXCHANGE = "/create-elementary-exchange";
    public static final String CREATE_PRODUCT_EXCHANGE = "/create-product-exchange";
    public static final String CONNECTOR = "/connectors";


    /************************
     *      IMPACTS APIs     *
     ************************/

    public static final String IMPACT = "/impacts";
    public static final String GET_ALL_IMPACT_CATEGORIES = "/impact-categories";
    public static final String GET_IMPACT_CATEGORY_BY_ID = "/impact-categories/{id}";

    public static final String GET_ALL_IMPACT_METHODS = "/impact-methods";
    public static final String GET_IMPACT_METHOD_BY_ID = "/impact-methods/{id}";
    public static final String GET_CATEGORY_BY_METHOD_ID = "/impact-methods/{id}/categories";
    public static final String GET_ALL_MIDPOINT_FACTORS = "/midpoints";
    public static final String GET_MIDPOINT_FACTOR_BY_ID = "/midpoints/{id}";

    /************************
     *      PROJECTS APIs     *
     ************************/

    public static final String PROJECT = "/projects";
    public static final String GET_PROJECT_LIST_BY_METHOD_ID = "/projects-list/{id}";

}
