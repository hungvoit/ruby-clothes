package com.javaguides.clothesbabies.service;
import com.javaguides.clothesbabies.dto.UserRegistrationDto;
import org.springframework.validation.BindingResult;

public interface ValidationService {
    BindingResult validateUser(UserRegistrationDto registrationDto, BindingResult result);
}
