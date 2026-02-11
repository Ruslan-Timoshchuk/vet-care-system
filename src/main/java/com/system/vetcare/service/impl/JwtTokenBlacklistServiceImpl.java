package com.system.vetcare.service.impl;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.system.vetcare.service.JwtTokenBlacklistService;

@Service
public class JwtTokenBlacklistServiceImpl implements JwtTokenBlacklistService {

    private final Set<String> tokenBlackList = new HashSet<>();
    
    @Override
    public boolean isBlacklisted(String token) {
        return tokenBlackList.contains(token);
    }
    
    @Override
    public void addTokenToBlacklist(String token) {
        tokenBlackList.add(token);
    }
    
}