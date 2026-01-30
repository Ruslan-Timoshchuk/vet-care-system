package com.system.vetcare.service;

import java.util.Set;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateToken(String userEmail, Set<String> authorityNames, Integer validTime);

    boolean isBlacklisted(String token);

    void addTokenToBlacklist(String token);

    Claims extractClaims(String token);

    String extractEmail(Claims claims);

    Set<String> extractAuthorityNames(Claims claims);

}