package com.BBVA.DiMo_S1.D_dtos.userDTO;

import com.BBVA.DiMo_S1.D_models.Role;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserSecurityDTO {

    private Long id;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;

    public UserSecurityDTO (Claims claims) {
        this.id = Long.valueOf(claims.getId());
        this.email = claims.getSubject();
        this.role = claims.get("role", String.class);
        this.createdAt = Instant.ofEpochMilli(claims.getIssuedAt().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        this.expirationDate = Instant.ofEpochMilli(claims.getExpiration().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}