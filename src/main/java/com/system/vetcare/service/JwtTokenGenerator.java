package com.system.vetcare.service;

import java.util.Set;

public interface JwtTokenGenerator {

    String generateAccessToken(String userEmail, Set<String> authorityNames);
    
    String generateRefreshToken(String userEmail);

}