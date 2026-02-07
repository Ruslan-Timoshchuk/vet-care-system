package com.system.vetcare.service;

public interface JwtTokenBlacklistService {

    boolean isBlacklisted(String token);

    void addTokenToBlacklist(String token);

}