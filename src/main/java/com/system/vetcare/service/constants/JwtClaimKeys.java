package com.system.vetcare.service.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtClaimKeys {

    public final String AUTHORITIES_CLAIM = "authorities";
    public final String TOKEN_TYPE_CLAIM = "type";
    public final String ACCESS_TOKEN_TYPE = "access";
    public final String REFRESH_TOKEN_TYPE = "refresh";
    
}