package com.example.cabonerfbe.config;

import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.services.JwtService;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userService;
    @Autowired
    private UserRepository userRepository;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

//        final String userId = request.getHeader("x-user-id");
//        final String userRole = request.getHeader("x-user-role");
//        final String userActive = request.getHeader("x-user-active");
        final String gateway_token = request.getHeader("gatewayToken");
        final String token;
        final String service_id;

//        if(!isNumeric(userId)){
//            sendErrorResponse(response,"User Id not valid");
//        }
//        if(!isNumeric(userRole)){
//            sendErrorResponse(response,"User role not valid");
//        }
//        if(!isNumeric(userActive)){
//            sendErrorResponse(response,"User verify status not valid");
//        }
//
//        int user_id = Integer.parseInt(userId);
//        int user_role = Integer.parseInt(userRole);
//        int user_active = Integer.parseInt(userActive);
//
//        if(user_active != 2){
//            switch (user_active){
//                case 1:
//                    sendErrorResponse(response,"Email has not been verified");
//                case 2:
//                    sendErrorResponse(response,"Email has suspended");
//            }
//        }



        try {
            // Kiểm tra nếu header Authorization có giá trị và bắt đầu bằng "Bearer"
            if (gateway_token == null || !gateway_token.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            token = gateway_token.substring(7);

            // Giải mã và lấy email người dùng từ token
            try {
                // Lấy email từ token, kiểm tra token từ Gateway
                service_id = jwtService.extractUsername(token, Constants.TOKEN_TYPE_GATEWAY);

                // Kiểm tra tính hợp lệ của token từ client đến Gateway
                if (service_id != null ) {
                    // Nếu token hợp lệ, chuyển đến token tiếp theo giữa Gateway và Microservice
                    jwtService.isGatewayTokenValid(token, Constants.TOKEN_TYPE_GATEWAY);
                }

                // Cho phép tiếp tục xử lý chuỗi lọc
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                // Bắt lỗi và trả về response hợp lý khi JWT không hợp lệ
                response.setStatus(422);
                response.setContentType("application/json");

                String jsonResponse = "{"
                        + "\"status\" : \"Error\","
                        + "\"message\": \"Error\","
                        + "\"data\": {\"accessToken\":\"Gateway token format is wrong\"}"
                        + "}";

                response.getWriter().write(jsonResponse);
                response.getWriter().flush();
            }

        } catch (ExpiredJwtException e) {
            // Ném ngoại lệ CustomExceptions với thông báo lỗi và thông tin liên quan
            sendErrorResponse(response,"Access token has expired");
        }
    }


    private boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return str.matches("-?\\d+");
    }
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String jsonResponse = "{"
                + "\"status\" : \"Error\","
                + "\"message\": \" Error \","
                + "\"data\": {"
                + "\"accessToken\":\""+errorMessage+ "\""
                + "}"
                + "}";

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

}
