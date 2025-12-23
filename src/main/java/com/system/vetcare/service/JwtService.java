package com.system.vetcare.service;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import io.jsonwebtoken.Claims;

public interface JwtService {
    
    String generateToken(String userEmail, List<SimpleGrantedAuthority> authorities,
            Integer validTime);
    
    boolean isValid(String token);
    
    boolean isBlacklisted(String token);
      
    void addTokenToBlacklist(String token);

    Claims extractClaims(String token);

    String extractEmail(Claims claims);

    List<SimpleGrantedAuthority> extractAuthorities(Claims claims);

}