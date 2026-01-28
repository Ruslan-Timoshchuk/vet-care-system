package com.system.vetcare.service.impl;

import static java.lang.System.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.system.vetcare.service.JwtService;
import java.util.Date;
import java.util.Set;
import javax.crypto.SecretKey;
import java.util.HashSet;
import java.util.List;

@Component
public class JwtServiceImpl implements JwtService {
	
    public static final String AUTHORITIES_CLAIM = "authorities";
    
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt.signing.key}")
    private String jwtSignKey;
    private final Set<String> tokenBlackList = new HashSet<>();

    @Override
    public String generateToken(String userEmail, List<SimpleGrantedAuthority> authorities, Integer validTime) {
        return Jwts
                 .builder()
                 .issuer(jwtIssuer)
                 .subject(userEmail)
                 .claim(AUTHORITIES_CLAIM, 
                        authorities
                          .stream()
                          .map(GrantedAuthority::getAuthority)
                          .toList())
                 .issuedAt(new Date(currentTimeMillis()))
                 .expiration(new Date(currentTimeMillis() + validTime))
                 .signWith(get())
                 .compact();
    }
    
    @Override
    public boolean isBlacklisted(String token) {
        return tokenBlackList.contains(token);
    }
    
    @Override
    public boolean isValid(String token) {
        return StringUtils.hasText(token);
    }

    @Override
    public void addTokenToBlacklist(String token) {
        tokenBlackList.add(token);
    }

    @Override
    public Claims extractClaims(String token) {
        return getJwtParser()
                 .parseSignedClaims(token)
                 .getPayload();
    }
    
    @Override
    public String extractEmail(Claims claims) {
        String email = claims.getSubject();
        if (StringUtils.hasText(email)) {
            return email;
        } else {
            throw new IllegalArgumentException("Email claim is missing or blank");
        }  
    }
    
    @Override
    public List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        if (claims.get(AUTHORITIES_CLAIM) instanceof List<?> list) {
            return list
                     .stream()
                     .map(String::valueOf)
                     .map(SimpleGrantedAuthority::new).toList();
        } else {
            throw new JwtException("Invalid authorities claim: expected array");
        }
    }
    
    private JwtParser getJwtParser() {
        return Jwts
                 .parser()
                 .verifyWith(get())
                 .build();
    }
    
    
    private SecretKey get() {
        byte[] keyBytes = Decoders
                            .BASE64
                            .decode(jwtSignKey);
        return Keys
                 .hmacShaKeyFor(keyBytes);
    }
    
}