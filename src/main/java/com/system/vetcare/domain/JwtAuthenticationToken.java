package com.system.vetcare.domain;

import static org.springframework.util.StringUtils.hasText;
import io.jsonwebtoken.JwtException;

public record JwtAuthenticationToken(
        String accessToken,
        String refreshToken) {
    
    public JwtAuthenticationToken {
        if (!hasText(accessToken) || !hasText(refreshToken)) {
            throw new JwtException("Access and refresh tokens must both be present");
        }
    }
    
}