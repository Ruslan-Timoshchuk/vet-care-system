package com.system.vetcare.service;

import io.jsonwebtoken.Claims;

public interface JwtClaimExtractor {

    Claims extractAccessTokenClaims(String token);

    Claims extractRefreshTokenClaims(String token);

}
