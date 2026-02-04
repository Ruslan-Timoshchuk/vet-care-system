package com.system.vetcare.service.impl;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import com.system.vetcare.service.JwtClaimsExtractor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtClaimsExtractorImpl implements JwtClaimsExtractor {

    private final SecretKey accessTokenSecretKey;
    private final SecretKey refreshTokenSecretKey;
    
    @Override
    public Claims extractAccessTokenClaims(String token) {
        return getJwtParser(accessTokenSecretKey)
                 .parseSignedClaims(token)
                 .getPayload();
    }
    
    @Override
    public Claims extractRefreshTokenClaims(String token) {
        return getJwtParser(refreshTokenSecretKey)
                 .parseSignedClaims(token)
                 .getPayload();
    }
    
    private JwtParser getJwtParser(SecretKey secretKey) {
        return Jwts
                 .parser()
                 .verifyWith(secretKey)
                 .build();
    }
    
}