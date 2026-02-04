package com.system.vetcare.controller.filter;

import static com.system.vetcare.payload.JwtMarkers.*;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.system.vetcare.service.AuthorityService;
import com.system.vetcare.service.JwtClaimsExtractor;
import com.system.vetcare.service.JwtCookiesService;
import com.system.vetcare.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final JwtCookiesService jwtCookiesService;
    private final AuthorityService authorityService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final SecurityContext securityContext = SecurityContextHolder.getContext();
            if (isNull(securityContext.getAuthentication())) {
                final Map<String, String> jwtTokens = jwtCookiesService.extractJwtTokens(request.getCookies());
                final String jwtAccessToken = jwtTokens.get(ACCESS_TOKEN);
                if (hasText(jwtAccessToken) && !jwtService.isBlacklisted(jwtAccessToken)) {
                    final WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource()
                            .buildDetails(request);
                    final Authentication authentication = buildAuthenticationToken(jwtAccessToken,
                            webAuthenticationDetails);
                    securityContext.setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private Authentication buildAuthenticationToken(String accessToken, WebAuthenticationDetails details) {
        final Claims claims = jwtClaimsExtractor.extractAccessTokenClaims(accessToken);
        final String email = jwtService.extractEmail(claims);
        final Set<String> authorityNames = jwtService.extractAuthorityNames(claims);
        final Set<SimpleGrantedAuthority> authorities = authorityService.toGrantedAuthorities(authorityNames);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                null, authorities);
        authenticationToken.setDetails(details);
        return authenticationToken;
    }

}