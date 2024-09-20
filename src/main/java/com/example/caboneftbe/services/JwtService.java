package com.example.caboneftbe.services;

import com.example.caboneftbe.converter.UserVerifyStatusConverter;
import com.example.caboneftbe.dto.UserVerifyStatusDto;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${app.access_token_secret_key}")
    private String accessTokenSecretKey;

    @Value("${app.refresh_token_secret_key}")
    private String refreshTokenSecretKey;

    @Value("${app.email_verify_token_secret_key}")
    private String emailVerifyTokenSecretKey;

    @Value("${app.forgot_password_token_secret_key}")
    private String forgotPasswordTokenSecretKey;

    @Autowired
    UserRepository userRepository;

    public String extractUsername(String token, String tokenType) {
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    public <T> T extractClaim(String token, String tokenType, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, Constants.TOKEN_TYPE_ACCESS, 3600000);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, Constants.TOKEN_TYPE_REFRESH, 64000000);
    }

    public String generateEmailVerifyToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, Constants.TOKEN_TYPE_EMAIL_VERIFY, 3600000);
    }

    public String generateForgotPasswordToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, Constants.TOKEN_TYPE_FORGOT_PASSWORD, 3600000);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, String secretKey, long expiration){
        int token_type = 0;
        switch (secretKey){
            case Constants.TOKEN_TYPE_ACCESS:
                token_type = 1;
                break;
            case Constants.TOKEN_TYPE_REFRESH:
                token_type = 2;
                break;
            case Constants.TOKEN_TYPE_EMAIL_VERIFY:
                token_type = 3;
                break;
            case Constants.TOKEN_TYPE_FORGOT_PASSWORD:
                token_type = 4;
                break;
        }
        var username = userRepository.findByEmail(userDetails.getUsername()).get();

        extraClaims.put("token_type", token_type);
        UserVerifyStatusDto verifyStatusDto = UserVerifyStatusConverter.INSTANCE.fromUserVerifyStatusToUserVerifyStatusDto(username.getUserVerifyStatus());
        extraClaims.put("user_verify_status", verifyStatusDto.getId());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(secretKey), SignatureAlgorithm.HS256)
                .setHeaderParam("typ","JWT")
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, String tokenType) {
        final String username = extractUsername(token, tokenType);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, tokenType);
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

    private Key getSignInKey(String tokenType) {
        String secretKey;
        switch (tokenType) {
            case Constants.TOKEN_TYPE_ACCESS:
                secretKey = accessTokenSecretKey;
                break;
            case Constants.TOKEN_TYPE_REFRESH:
                secretKey = refreshTokenSecretKey;
                break;
            case Constants.TOKEN_TYPE_EMAIL_VERIFY:
                secretKey = emailVerifyTokenSecretKey;
                break;
            case Constants.TOKEN_TYPE_FORGOT_PASSWORD:
                secretKey = forgotPasswordTokenSecretKey;
                break;
            default:
                throw new IllegalArgumentException("Unknown token type: " + tokenType);
        }
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
