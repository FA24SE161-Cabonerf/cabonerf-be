package com.example.cabonerfbe.config;

import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.UserVerifyStatus;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.repositories.UserVerifyStatusRepository;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.util.UUIDUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserVerifyStatusRepository userVerifyStatusRepository;

    private static final String SWAGGER_UI_PATH = "/swagger-ui/";
    private static final String API_DOCS_PATH = "/v3/api-docs";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (isSwaggerRequest(requestURI) || isPublicPath(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userId = request.getHeader("x-user-id");
        final String userRole = request.getHeader("x-user-role");
        final String userActive = request.getHeader("x-user-active");
        final String gateway_token = request.getHeader("gatewayToken");
        final String token;
        final String service_id;
        Map<String, String> errorData = new HashMap<>();
        if (userId == null || userId.isEmpty()) {
            errorData.put("x-user-id", "User ID is required");
        }
        if (userRole == null || userRole.isEmpty()) {
            errorData.put("x-user-role", "User role is required");
        }
        if (userActive == null || userActive.isEmpty()) {
            errorData.put("x-user-active", "User active status is required");
        }
        if (gateway_token == null || gateway_token.isEmpty()) {
            errorData.put("gatewayToken", "Gateway token is required");
        }
        if (!errorData.isEmpty()) {
            sendErrorResponse(response, errorData);
            return;
        }
        if (!isValidUUID(userId)) {
            errorData.put("x-user-id", "Invalid UUID format for user id");
        }
        if (!isValidUUID(userRole)) {
            errorData.put("x-user-role", "Invalid UUID format for user role");
        }
        if (userActive == null || !userActive.matches("\\d+")) {
            errorData.put("x-user-active", "User active invalid");
        }
        if (!errorData.isEmpty()) {
            sendErrorResponse(response, errorData);
            return;
        }
        UUID user_id = UUIDUtil.fromString(userId);
        UUID user_role = UUIDUtil.fromString(userRole);
        int user_active = Integer.parseInt(userActive);


        if (user_active != 2) {
            switch (user_active) {
                case 1:
                    errorData.put("userVerifyActive", "Email has not been verified");
                    break;
                case 3:
                    errorData.put("userVerifyActive", "Email has suspended");
                    break;
                default:
                    errorData.put("userVerifyActive", "user active id invalid");
                    break;
            }
        }

        if (!errorData.isEmpty()) {
            sendErrorResponse(response, errorData);
            return;
        }

        try {
            // Kiểm tra nếu header Authorization có giá trị và bắt đầu bằng "Bearer"
            if (gateway_token.isEmpty()) {
                errorData.put("gatewayToken", "Gateway token is invalid");
                sendErrorResponse(response, errorData);
                return;
            }

            token = gateway_token;

            try {
                service_id = jwtService.extractServiceId(token);

                if (service_id != null) {

                    if (!jwtService.isGatewayTokenValid(token, Constants.TOKEN_TYPE_SERVICE)) {
                        errorData.put("gatewayToken", "Gateway token is invalid");
                    }
                }

                filterChain.doFilter(request, response);
            } catch (Exception e) {
                errorData.put("gatewayToken", "Gateway token is invalid");
                sendErrorResponse(response, errorData);
            }


        } catch (ExpiredJwtException e) {
            errorData.put("gatewayToken", "Gateway token has expired");
        }
    }


    private boolean isValidUUID(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isSwaggerRequest(String uri) {
        return uri.contains(SWAGGER_UI_PATH) || uri.contains(API_DOCS_PATH);
    }

    private boolean isPublicPath(String uri) {
        return uri.contains("/register") || uri.contains("/login");
    }

    private void sendErrorResponse(HttpServletResponse response, Map<String, String> errorData) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append("\"data\": {");
        for (Map.Entry<String, String> entry : errorData.entrySet()) {
            dataBuilder.append("\"")
                    .append(entry.getKey())
                    .append("\": \"")
                    .append(entry.getValue())
                    .append("\",");
        }
        dataBuilder.deleteCharAt(dataBuilder.length() - 1);
        dataBuilder.append("}");

        String jsonResponse = "{"
                + "\"status\": \"Error\","
                + "\"message\": \"Error\","
                + dataBuilder.toString()
                + "}";

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }


}
