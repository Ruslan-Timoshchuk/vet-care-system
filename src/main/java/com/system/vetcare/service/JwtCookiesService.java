package com.system.vetcare.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface JwtCookiesService {

    HttpHeaders issueJwtCookies(String email, List<SimpleGrantedAuthority> authorities);

    HttpHeaders refreshJwtCookies(Cookie[] cookies);

    HttpHeaders revokeJwtCookies(Cookie[] cookies);

    Map<String, String> extractJwtTokens(Cookie[] cookies);

}