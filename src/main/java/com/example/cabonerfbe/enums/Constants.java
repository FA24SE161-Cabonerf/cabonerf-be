package com.example.cabonerfbe.enums;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Constants {
    public static final String RESPONSE_STATUS_ERROR = "Error";
    public static final String RESPONSE_STATUS_SUCCESS = "Success";
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";
    public static final String TOKEN_TYPE_EMAIL_VERIFY = "email_verify";
    public static final String TOKEN_TYPE_FORGOT_PASSWORD = "forgot_password";
    public static final String TOKEN_TYPE_INVITE_ORGANIZATION = "invite_organization";
    public static final String TOKEN_TYPE_GATEWAY = "client-gateway";
    public static final String TOKEN_TYPE_SERVICE = "gateway-service";
    public static final boolean STATUS_TRUE = true;
    public static final boolean STATUS_FALSE = false;
    public static final String UNIT_GROUP_TYPE_NORMAL = "normal";
    public static final Boolean IS_DEFAULT_TRUE = true;
    public static final String PRODUCT_EXCHANGE = "Product";
    public static final String ELEMENTARY_EXCHANGE = "Elementary";

    public static final String COMPUTE_EXCHANGE_TYPE_CREATE = "Create";
    public static final String COMPUTE_EXCHANGE_TYPE_UPDATE = "Update";
    public static final String COMPUTE_EXCHANGE_TYPE_DELETE = "Delete";

    public static final BigDecimal NEW_OVERALL_FLOW = BigDecimal.ZERO;
    public static final BigDecimal DEFAULT_OVERALL_IMPACT_CONTRIBUTION = BigDecimal.ZERO;
    public static final BigDecimal DEFAULT_SYSTEM_LEVEL = BigDecimal.ZERO;
    public static final BigDecimal DEFAULT_PREVIOUS_PROCESS_VALUE = BigDecimal.ZERO;
    public static final BigDecimal DEFAULT_OVERALL_PRODUCT_FLOW_REQUIRED = BigDecimal.ZERO;
    public static final BigDecimal BASE_UNIT_LEVEL = BigDecimal.ZERO;

    public static final int BIG_DECIMAL_DEFAULT_SCALE = 60;

    public static final String DEFAULT_USER_IMAGE = "https://cabonerf-storage.s3.ap-southeast-1.amazonaws.com/image/default_user.png";

    public static final String DELETE_CONNECTOR_TYPE_EXCHANGE = "exchange";
    public static final String DELETE_CONNECTOR_TYPE_PROCESS = "process";
    public static final String DEFAULT_ORGANIZATION = "Personal";
    public static final String LCA_STAFF = "LCA Staff";
    public static final String ORGANIZATION_MANAGER = "Organization Manager";

    public static final String VERIFY_STATUS_PENDING = "Pending";

    public static final String USER_ID_HEADER = "x-user-id";

    public static final boolean IS_LIB_TRUE = true;
    public static final boolean IS_LIB_FALSE = false;
    public static final int VERSION_ONE = 1;


    public static final String BOUNDARY_GATE = "Gate";
    public static final String BOUNDARY_CRADLE = "Cradle";
    public static final String BOUNDARY_GRAVE = "Grave";

    // fixed because requirements unexpectedly changed.
    // Ecoinvent dataset
    public static final List<UUID> DATASET_LIST = Arrays.asList(UUID.fromString("461282ad-3ee6-408b-80be-ec28d792f891"));

}
