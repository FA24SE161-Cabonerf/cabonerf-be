package com.example.cabonerfbe.services;

import com.example.cabonerfbe.converter.UserVerifyStatusConverter;
import com.example.cabonerfbe.dto.UserVerifyStatusDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.EmailVerificationToken;
import com.example.cabonerfbe.repositories.EmailVerificationTokenRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {
//    private final Dotenv dotenv = Dotenv.load();

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailVerificationTokenRepository evtRepository;
    @Value("${app.access_token_secret_key}")
    private String accessTokenSecretKey;
    @Value("${app.refresh_token_secret_key}")
    private String refreshTokenSecretKey;

//    private final String gatewayTokenSecretKey = dotenv.get("CLIENT_GATEWAY_SECRET_KEY");
//
//    private final String mainIdServiceKey = dotenv.get("MAIN_SERVICE_ID_KEY");
//
//    private final long ACCESS_TOKEN_EXPIRATION = Long.parseLong(Objects.requireNonNull(dotenv.get("ACCESS_TOKEN_EXPIRATION")));
//    private final long REFRESH_TOKEN_EXPIRATION = Long.parseLong(Objects.requireNonNull(dotenv.get("REFRESH_TOKEN_EXPIRATION")));
//    private final long EMAIL_VERIFY_TOKEN_EXPIRATION = Long.parseLong(Objects.requireNonNull(dotenv.get("EMAIL_VERIFY_TOKEN_EXPIRATION")));
//    private final long FORGOT_EXPIRATION = Long.parseLong(Objects.requireNonNull(dotenv.get("FORGOT_TOKEN_EXPIRATION")));
//    private final long GATEWAY_TOKEN_EXPIRATION = Long.parseLong(Objects.requireNonNull(dotenv.get("GATEWAY_TOKEN_EXPIRATION")));
    @Value("${app.email_verify_token_secret_key}")
    private String emailVerifyTokenSecretKey;
    @Value("${app.forgot_password_token_secret_key}")
    private String forgotPasswordTokenSecretKey;
    @Value("${CLIENT_GATEWAY_SECRET_KEY}")
    private String clientGatewaySecretKey;
    @Value("${MAIN_SERVICE_ID_KEY}")
    private String mainServiceIdKey;
    @Value("${GATEWAY_SERVICE_SECRET_KEY}")
    private String gatewayServiceSecretKey;
    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private long ACCESS_TOKEN_EXPIRATION;
    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private long REFRESH_TOKEN_EXPIRATION;
    @Value("${EMAIL_VERIFY_TOKEN_EXPIRATION}")
    private long EMAIL_VERIFY_TOKEN_EXPIRATION;
    @Value("${FORGOT_TOKEN_EXPIRATION}")
    private long FORGOT_EXPIRATION;
    @Value("${GATEWAY_TOKEN_EXPIRATION}")
    private long GATEWAY_TOKEN_EXPIRATION;

    public String extractUsername(String token, String tokenType) {
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    public String extractServiceId(String token) {
        return extractAllClaims(token, Constants.TOKEN_TYPE_SERVICE).get("id", String.class);
    }

    public <T> T extractClaim(String token, String tokenType, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, Constants.TOKEN_TYPE_ACCESS, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, Constants.TOKEN_TYPE_REFRESH, REFRESH_TOKEN_EXPIRATION);
    }

    public String generateEmailVerifyToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, Constants.TOKEN_TYPE_EMAIL_VERIFY, EMAIL_VERIFY_TOKEN_EXPIRATION);
    }

    public String generateForgotPasswordToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, Constants.TOKEN_TYPE_FORGOT_PASSWORD, FORGOT_EXPIRATION);
    }

    public String generateGatewayToken() {
        return generateGatewayToken(clientGatewaySecretKey, GATEWAY_TOKEN_EXPIRATION);
    }


    private String generateGatewayToken(String secretKey, long expiration) {
        return Jwts
                .builder()
                .claim("service_id", mainServiceIdKey)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, String tokenType) {
        final String username = extractUsername(token, tokenType);
        return username.equals(userDetails.getUsername());
    }

    public boolean isGatewayTokenValid(String token, String tokenType) {
        final String service_id = extractServiceId(token).trim();
        return service_id.equals(mainServiceIdKey);
    }

    public boolean isTokenExpired(String token, String tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    private Date extractExpiration(String token, String tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token, String tokenType) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(tokenType))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // Phương thức tạo token với các loại token khác nhau
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, String tokenType, long expiration) {
        int token_type_id = 0;

        // Xác định loại token
        switch (tokenType) {
            case Constants.TOKEN_TYPE_ACCESS:
                token_type_id = 1;
                break;
            case Constants.TOKEN_TYPE_REFRESH:
                token_type_id = 2;
                break;
            case Constants.TOKEN_TYPE_EMAIL_VERIFY:
                token_type_id = 3;
                break;
            case Constants.TOKEN_TYPE_FORGOT_PASSWORD:
                token_type_id = 4;
                break;
            default:
                throw new IllegalArgumentException("Unknown token type: " + tokenType);
        }

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        var user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));

        // Chuyển đổi trạng thái verify của người dùng
        UserVerifyStatusDto verifyStatusDto = UserVerifyStatusConverter.INSTANCE
                .fromUserVerifyStatusToUserVerifyStatusDto(user.getUserVerifyStatus());

        int verifyId = switch (verifyStatusDto.getStatusName()) {
            case "Pending" -> 1;
            case "Verified" -> 2;
            case "Suspended" -> 3;
            default -> 0;
        };
        // Thêm các claim vào token
        extraClaims.put("user_verify_status", verifyId);
        extraClaims.put("user_id", user.getId());
        extraClaims.put("role_id", user.getRole().getId());
        extraClaims.put("token_type", token_type_id);

        // Tạo JWT token
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(tokenType), SignatureAlgorithm.HS256) // Sử dụng key từ token type
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    // Lấy key bí mật tương ứng dựa trên loại token
    private Key getSignInKey(String tokenType) {
        String secretKey;

        switch (tokenType) {
            case Constants.TOKEN_TYPE_ACCESS:
            case Constants.TOKEN_TYPE_REFRESH:
                secretKey = clientGatewaySecretKey;  // Key cho ACCESS và REFRESH
                break;
            case Constants.TOKEN_TYPE_EMAIL_VERIFY:
                secretKey = emailVerifyTokenSecretKey;  // Key cho email verification
                break;
            case Constants.TOKEN_TYPE_FORGOT_PASSWORD:
                secretKey = forgotPasswordTokenSecretKey;  // Key cho quên mật khẩu
                break;
            case Constants.TOKEN_TYPE_SERVICE:
                secretKey = gatewayServiceSecretKey;  // Key cho các dịch vụ khác
                break;
            default:
                throw new IllegalArgumentException("Unknown token type: " + tokenType);
        }

        // Giải mã Base64 secret key
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public EmailVerificationToken checkToken(String token) {
        if (!token.startsWith("Bearer ")) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("Email verify token", "Email verify token not valid"));
        }

        String verifyToken = token.substring(7);

        try {
            String userEmail = this.extractUsername(verifyToken, "email_verify");
            if (this.isTokenExpired(verifyToken, "email_verify")) {
                throw CustomExceptions.unauthorized(
                        Constants.RESPONSE_STATUS_ERROR,
                        Map.of("emailVerify", "Email verify token is expired")
                );
            }
        } catch (JwtException e) {
            throw CustomExceptions.validator(
                    Constants.RESPONSE_STATUS_ERROR,
                    Map.of("emailVerify", "Email verify token format is wrong")
            );
        }

        EmailVerificationToken _token = evtRepository.findByToken(verifyToken);

        if (_token == null) {
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, Map.of("Email verify token", "Email verify token not exist"));
        }
        if (!_token.isValid()) {
            throw CustomExceptions.unauthorized(Constants.RESPONSE_STATUS_ERROR, Map.of("Email verify token", "Email verify token not valid"));
        }
        return _token;
    }
}
