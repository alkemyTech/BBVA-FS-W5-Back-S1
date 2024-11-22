package com.BBVA.DiMo_S1.E_config;

import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.security.jwt.login-key}")
    private String loginKey;
    @Value("${application.security.jwt.login-key-expiration}")
    private String loginTokenExpiration;

    public String generateToken(
            final Long userId,
            final String email,
            final String role
    ) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);

        final LocalDateTime dueDateToken = LocalDateTime
                .now()
                .plusMinutes(Long.parseLong(loginTokenExpiration))
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();
        final Date expirationDate = Date.from(dueDateToken.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setId(userId.toString())
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(loginKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(final String token, UserDetails userDetails) {
        final String userEmail = extractUsername(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public <T> T extractClaim(final String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
    
    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(
            final String token
    ) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UserSecurityDTO validateAndGetSecurity(
            final String token
    ) {
        Claims claims = extractAllClaims(token);
        UserSecurityDTO security = new UserSecurityDTO(claims);
        return security;
    }

    public String extraerToken (HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        return token.substring(7);
    }
}
