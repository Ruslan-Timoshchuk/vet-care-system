package com.system.vetcare.controller.filter;

import static com.system.vetcare.payload.JwtMarkers.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.system.vetcare.service.JwtCookiesService;
import com.system.vetcare.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtCookiesService cookiesService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            validateAccessTokenRequest(request);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

	private void validateAccessTokenRequest(HttpServletRequest request) {
		Map<String, String> jwtTokens = cookiesService.extractJwtTokens(request.getCookies());
		if (jwtTokens.containsKey(ACCESS_TOKEN)) {
			final String jwtAccessToken = jwtTokens.get(ACCESS_TOKEN);
			if (jwtService.isValid(jwtAccessToken) && !jwtService.isBlacklisted(jwtAccessToken)) {
			    Claims claims = jwtService.extractClaims(jwtAccessToken);
				final String email = jwtService.extractEmail(claims);
				final List<SimpleGrantedAuthority> authorities = jwtService.extractAuthorities(claims);
				if (!email.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					        email, null, authorities);
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
	}
	
}