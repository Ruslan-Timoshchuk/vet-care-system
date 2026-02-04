package com.system.vetcare.service;

import java.util.Set;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateAccessToken(String userEmail, Set<String> authorityNames);
    
    String generateRefreshToken(String userEmail);

    boolean isBlacklisted(String token);

    void addTokenToBlacklist(String token);

    String extractEmail(Claims claims);

    Set<String> extractAuthorityNames(Claims claims);

}