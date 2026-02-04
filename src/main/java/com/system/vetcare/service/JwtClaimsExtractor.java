package com.system.vetcare.service;

import io.jsonwebtoken.Claims;

public interface JwtClaimsExtractor {

    Claims extractAccessTokenClaims(String token);

    Claims extractRefreshTokenClaims(String token);

}
