package com.system.vetcare.config;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtSecretKeyConfig {

    @Value("${jwt.access.signing.key}")
    private String accessSignKey;
    @Value("${jwt.refresh.signing.key}")
    private String refreshSignKey;
    
    @Bean
    public SecretKey accessTokenSecretKey() {
        return Keys
                 .hmacShaKeyFor(toBytes(accessSignKey));
    }
    
    @Bean
    public SecretKey refreshTokenSecretKey() {
        return Keys
                 .hmacShaKeyFor(toBytes(refreshSignKey));
    }
    
    private byte[] toBytes(String signingKey) {
        byte[] keyBytes = Decoders
                            .BASE64
                            .decode(signingKey);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT signing key must be at least 256 bits");
        }
        return keyBytes;
    }
    
}