package com.system.vetcare.service;

import com.system.vetcare.domain.User;
import com.system.vetcare.payload.request.RegistrationRequest;

public interface UserService {
    
    User save(RegistrationRequest registerRequest);

    void updateLoginTimestamp(User user);

    User loadUserByUsername(String email);
    
}