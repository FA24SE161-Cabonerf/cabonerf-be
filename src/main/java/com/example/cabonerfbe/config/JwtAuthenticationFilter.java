package com.example.cabonerfbe.config;

import com.example.cabonerfbe.enums.Constants;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String access_token;
        final String userEmail;

        try {
            // Kiểm tra nếu header Authorization có giá trị và bắt đầu bằng "Bearer"
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            access_token = authHeader.substring(7);

            // Giải mã và lấy email người dùng từ token
            try{
                userEmail = jwtService.extractUsername(access_token, Constants.TOKEN_TYPE_ACCESS);



            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load thông tin người dùng từ dịch vụ UserDetails
                UserDetails userDetails = this.userService.loadUserByUsername(userEmail);

                // Kiểm tra tính hợp lệ của token
                if (jwtService.isTokenValid(access_token, userDetails,Constants.TOKEN_TYPE_ACCESS)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Đặt thông tin xác thực vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Cho phép tiếp tục xử lý chuỗi lọc
            filterChain.doFilter(request, response);
            }catch (Exception e){
                response.setStatus(422);

                response.setContentType("application/json");
                String jsonResponse = "{"
                        + "\"status\" : \"Error\","
                        + "\"message\": \"Error\","
                        + "\"data\": {" +
                        "\"accessToken\":\"Access token format is wrong\"" +
                        "}"
                        + "}";

                response.getWriter().write(jsonResponse);
                response.getWriter().flush();
            }
        } catch (ExpiredJwtException e) {
            // Ném ngoại lệ CustomExceptions với thông báo lỗi và thông tin liên quan
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");
            String jsonResponse = "{"
                    + "\"status\" : \"Error\","
                    + "\"message\": \"Error\","
                    + "\"data\": {" +
                    "\"accessToken\":\"Access token has expired\"" +
                    "}"
                    + "}";

            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        }
    }

}
