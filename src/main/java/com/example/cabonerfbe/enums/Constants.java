package com.example.cabonerfbe.enums;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The class Constants.
 *
 * @author SonPHH.
 */
public class Constants {
    /**
     * The constant RESPONSE_STATUS_ERROR.
     */
    public static final String RESPONSE_STATUS_ERROR = "Error";
    /**
     * The constant RESPONSE_STATUS_SUCCESS.
     */
    public static final String RESPONSE_STATUS_SUCCESS = "Success";
    /**
     * The constant TOKEN_TYPE_ACCESS.
     */
    public static final String TOKEN_TYPE_ACCESS = "access";
    /**
     * The constant TOKEN_TYPE_REFRESH.
     */
    public static final String TOKEN_TYPE_REFRESH = "refresh";
    /**
     * The constant TOKEN_TYPE_EMAIL_VERIFY.
     */
    public static final String TOKEN_TYPE_EMAIL_VERIFY = "email_verify";
    /**
     * The constant TOKEN_TYPE_FORGOT_PASSWORD.
     */
    public static final String TOKEN_TYPE_FORGOT_PASSWORD = "forgot_password";
    /**
     * The constant TOKEN_TYPE_INVITE_ORGANIZATION.
     */
    public static final String TOKEN_TYPE_INVITE_ORGANIZATION = "invite_organization";
    /**
     * The constant TOKEN_TYPE_GATEWAY.
     */
    public static final String TOKEN_TYPE_GATEWAY = "client-gateway";
    /**
     * The constant TOKEN_TYPE_SERVICE.
     */
    public static final String TOKEN_TYPE_SERVICE = "gateway-service";
    /**
     * The constant STATUS_TRUE.
     */
    public static final boolean STATUS_TRUE = true;
    /**
     * The constant STATUS_FALSE.
     */
    public static final boolean STATUS_FALSE = false;
    /**
     * The constant UNIT_GROUP_TYPE_NORMAL.
     */
    public static final String UNIT_GROUP_TYPE_NORMAL = "normal";
    /**
     * The constant IS_DEFAULT_TRUE.
     */
    public static final Boolean IS_DEFAULT_TRUE = true;
    /**
     * The constant PRODUCT_EXCHANGE.
     */
    public static final String PRODUCT_EXCHANGE = "Product";
    /**
     * The constant ELEMENTARY_EXCHANGE.
     */
    public static final String ELEMENTARY_EXCHANGE = "Elementary";

    /**
     * The constant COMPUTE_EXCHANGE_TYPE_CREATE.
     */
    public static final String COMPUTE_EXCHANGE_TYPE_CREATE = "Create";
    /**
     * The constant COMPUTE_EXCHANGE_TYPE_UPDATE.
     */
    public static final String COMPUTE_EXCHANGE_TYPE_UPDATE = "Update";
    /**
     * The constant COMPUTE_EXCHANGE_TYPE_DELETE.
     */
    public static final String COMPUTE_EXCHANGE_TYPE_DELETE = "Delete";

    /**
     * The constant NEW_OVERALL_FLOW.
     */
    public static final BigDecimal NEW_OVERALL_FLOW = BigDecimal.ZERO;
    /**
     * The constant DEFAULT_OVERALL_IMPACT_CONTRIBUTION.
     */
    public static final BigDecimal DEFAULT_OVERALL_IMPACT_CONTRIBUTION = BigDecimal.ZERO;
    /**
     * The constant DEFAULT_SYSTEM_LEVEL.
     */
    public static final BigDecimal DEFAULT_SYSTEM_LEVEL = BigDecimal.ZERO;
    /**
     * The constant DEFAULT_PREVIOUS_PROCESS_VALUE.
     */
    public static final BigDecimal DEFAULT_PREVIOUS_PROCESS_VALUE = BigDecimal.ZERO;
    /**
     * The constant DEFAULT_OVERALL_PRODUCT_FLOW_REQUIRED.
     */
    public static final BigDecimal DEFAULT_OVERALL_PRODUCT_FLOW_REQUIRED = BigDecimal.ZERO;
    /**
     * The constant BASE_UNIT_LEVEL.
     */
    public static final BigDecimal BASE_UNIT_LEVEL = BigDecimal.ZERO;

    /**
     * The constant BIG_DECIMAL_DEFAULT_SCALE.
     */
    public static final int BIG_DECIMAL_DEFAULT_SCALE = 60;

    /**
     * The constant DEFAULT_USER_IMAGE.
     */
    public static final String DEFAULT_USER_IMAGE = "https://cabonerf-storage.s3.ap-southeast-1.amazonaws.com/image/default_user.png";

    /**
     * The constant DELETE_CONNECTOR_TYPE_EXCHANGE.
     */
    public static final String DELETE_CONNECTOR_TYPE_EXCHANGE = "exchange";
    /**
     * The constant DELETE_CONNECTOR_TYPE_PROCESS.
     */
    public static final String DELETE_CONNECTOR_TYPE_PROCESS = "process";
    /**
     * The constant DEFAULT_ORGANIZATION.
     */
    public static final String DEFAULT_ORGANIZATION = "Personal";
    /**
     * The constant LCA_STAFF.
     */
    public static final String LCA_STAFF = "LCA Staff";
    /**
     * The constant ORGANIZATION_MANAGER.
     */
    public static final String ORGANIZATION_MANAGER = "Organization Manager";

    /**
     * The constant VERIFY_STATUS_PENDING.
     */
    public static final String VERIFY_STATUS_PENDING = "Pending";

    /**
     * The constant USER_ID_HEADER.
     */
    public static final String USER_ID_HEADER = "x-user-id";

    /**
     * The constant IS_LIB_TRUE.
     */
    public static final boolean IS_LIB_TRUE = true;
    /**
     * The constant IS_LIB_FALSE.
     */
    public static final boolean IS_LIB_FALSE = false;
    /**
     * The constant VERSION_ONE.
     */
    public static final int VERSION_ONE = 1;


    /**
     * The constant BOUNDARY_GATE.
     */
    public static final String BOUNDARY_GATE = "Gate";
    /**
     * The constant BOUNDARY_CRADLE.
     */
    public static final String BOUNDARY_CRADLE = "Cradle";
    /**
     * The constant BOUNDARY_GRAVE.
     */
    public static final String BOUNDARY_GRAVE = "Grave";

    /**
     * The constant DATASET_LIST.
     */
// fixed because requirements unexpectedly changed.
    // Ecoinvent dataset
    public static final List<UUID> DATASET_LIST = List.of(UUID.fromString("461282ad-3ee6-408b-80be-ec28d792f891"));

    /**
     * The constant INVITE_ORGANIZATION_SUBJECT.
     */
    public static final String INVITE_ORGANIZATION_SUBJECT = "Invite Organization";
    /**
     * The constant CREATE_ORGANIZATION_SUBJECT.
     */
    public static final String CREATE_ORGANIZATION_SUBJECT = "Create Organization";

    /**
     * The constant CABONERF_TITLE.
     */
    public static final String CABONERF_TITLE = "Cabonerf";
    /**
     * The constant CABONERF_GMAIL.
     */
    public static final String CABONERF_GMAIL = "cabonerf@gmail.com";


}
