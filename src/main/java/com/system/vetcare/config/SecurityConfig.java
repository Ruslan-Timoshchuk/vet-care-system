package com.system.vetcare.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.http.HttpMethod.*;
import static java.util.stream.Collectors.toList;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import com.system.vetcare.controller.filter.JwtAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
 
    public static final Set<String> ALLOWED_AUTHENTICATION_API_POST = Set.of(
            "/api/v1/security/login", 
            "/api/v1/security/registration", 
            "/api/v1/security/refresh",
            "/api/v1/security/validate_email");
    public static final Set<String> ALLOWED_AUTHORITIES_API_GET = Set.of("/api/v1/authorities/all");
    
    @Bean
    public SecurityFilterChain filterChain(
            final HttpSecurity httpSecurity,
            final JwtAuthenticationFilter jwtAuthenticationFilter,
            final RequestMatcher publicEndpointsMatcher) throws Exception {
        httpSecurity.csrf().disable()
          .sessionManagement().sessionCreationPolicy(STATELESS)
                   .and()
                   .authorizeRequests()
                   .requestMatchers(publicEndpointsMatcher).permitAll()
                   .anyRequest()
                   .authenticated()
                   .and()
                   .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public RequestMatcher publicEndpointsMatcher() {
        return new OrRequestMatcher(
                Stream
                 .concat(ALLOWED_AUTHENTICATION_API_POST.stream()
                          .map(url -> new AntPathRequestMatcher(url, POST.name())),
                        ALLOWED_AUTHORITIES_API_GET.stream()
                          .map(url -> new AntPathRequestMatcher(url, GET.name())))
                .collect(toList()));
    }

}