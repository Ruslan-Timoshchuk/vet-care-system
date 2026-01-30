
package com.system.vetcare.service.impl;

import static java.util.stream.Collectors.toUnmodifiableSet;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import com.system.vetcare.domain.Authority;
import com.system.vetcare.payload.response.AuthorityDetailsResponse;
import com.system.vetcare.repository.AuthorityRepository;
import com.system.vetcare.service.AuthorityService;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public List<AuthorityDetailsResponse> findAll() {
        return authorityRepository
                 .findAll()
                 .stream()
                 .map(authority -> new AuthorityDetailsResponse(authority.getId(), authority.getTitle().name()))
                 .toList();
    }

    @Override
    public Set<Authority> findAllById(List<Integer> authorityIds) {
        return authorityRepository
                 .findAllById(authorityIds)
                 .stream()
                 .collect(toUnmodifiableSet());
    }

    @Override
    public Set<String> toAuthorityNames(Set<Authority> authorities) {
        return authorities
                 .stream()
                 .map(authority -> authority.getTitle().name())
                 .collect(toUnmodifiableSet());
    }

    @Override
    public Set<SimpleGrantedAuthority> toGrantedAuthorities(Set<String> names) {
        return names
                 .stream()
                 .map(SimpleGrantedAuthority::new)
                 .collect(toUnmodifiableSet());
    }

}