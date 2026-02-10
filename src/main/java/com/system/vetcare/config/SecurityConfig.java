package com.system.vetcare.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
 
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http,
            final JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf().disable()
          .sessionManagement().sessionCreationPolicy(STATELESS)
                   .and()
                   .authorizeHttpRequests()
                   .antMatchers("/api/v1/security/login").permitAll()
                   .antMatchers("/api/v1/security/registration").permitAll()
                   .antMatchers("/api/v1/security/refresh").permitAll()
                   .antMatchers("/api/v1/security/validate_email").permitAll()
                   .antMatchers("/api/v1/authorities/all").permitAll()
                   .anyRequest()
                   .authenticated()
                   .and()
                   .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
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
    public RequestMatcher requestMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/security/login", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/api/v1/security/registration", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/api/v1/security/refresh", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/api/v1/security/validate_email", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/api/v1/authorities/all", HttpMethod.POST.name()));
    }

}