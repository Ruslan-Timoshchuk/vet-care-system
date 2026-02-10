package com.system.vetcare.service;

import javax.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import com.system.vetcare.domain.JwtAuthenticationToken;

public interface JwtCookiesService {

    HttpHeaders issueJwtCookies(JwtAuthenticationToken authenticationToken);

    HttpHeaders revokeJwtCookies();

    String extractJwtToken(Cookie[] cookies, String cookieName);

}