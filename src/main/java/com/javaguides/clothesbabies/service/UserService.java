package com.javaguides.clothesbabies.service;

import com.javaguides.clothesbabies.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
     void saveUser(UserRegistrationDto dto);
}
