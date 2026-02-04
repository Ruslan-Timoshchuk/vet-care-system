package com.system.vetcare.service.impl;

import static java.lang.System.*;
import static java.util.stream.Collectors.toUnmodifiableSet;
import java.security.Key;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.system.vetcare.service.JwtService;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
	
    public static final String AUTHORITIES_CLAIM = "authorities";
    
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${access.token.life.time}")
    private Integer accessTokenLifeTime;
    @Value("${refresh.token.life.time}")
    private Integer refreshTokenLifeTime; 
    
    private final Key accessTokenSecretKey;
    private final Key refreshTokenSecretKey;
    
    private final Set<String> tokenBlackList = new HashSet<>();

    @Override
    public String generateAccessToken(String userEmail, Set<String> authorityNames) {
        return Jwts
                 .builder()
                 .issuer(jwtIssuer)
                 .subject(userEmail)
                 .claim(AUTHORITIES_CLAIM, authorityNames)
                 .issuedAt(new Date(currentTimeMillis()))
                 .expiration(new Date(currentTimeMillis() + accessTokenLifeTime))
                 .signWith(accessTokenSecretKey)
                 .compact();
    }
    
    @Override
    public String generateRefreshToken(String userEmail) {
        return Jwts
                 .builder()
                 .issuer(jwtIssuer)
                 .subject(userEmail)
                 .issuedAt(new Date(currentTimeMillis()))
                 .expiration(new Date(currentTimeMillis() + refreshTokenLifeTime))
                 .signWith(refreshTokenSecretKey)
                 .compact();
    }
    
    @Override
    public boolean isBlacklisted(String token) {
        return tokenBlackList.contains(token);
    }
    
    @Override
    public void addTokenToBlacklist(String token) {
        tokenBlackList.add(token);
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
    public Set<String> extractAuthorityNames(Claims claims) {
        if (claims.get(AUTHORITIES_CLAIM) instanceof List<?> list) {
            return list
                     .stream()
                     .map(String::valueOf)
                     .collect(toUnmodifiableSet());
        } else {
            throw new JwtException("Invalid authorities claim: expected array");
        }
    }
    
}