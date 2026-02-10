package com.system.vetcare.service;

import com.system.vetcare.domain.JwtAuthenticationToken;
import com.system.vetcare.domain.User;

public interface JwtAuthenticationService {

    JwtAuthenticationToken issueAuthenticationToken(User user);

    JwtAuthenticationToken refreshAuthenticationToken(String jwtRefreshToken);

    void revokeAuthenticationToken(String jwtRefreshToken);

}