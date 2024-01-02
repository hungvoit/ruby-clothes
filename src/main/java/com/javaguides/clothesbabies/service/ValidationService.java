package com.javaguides.clothesbabies.service;
import com.javaguides.clothesbabies.dto.UserDto;
import org.springframework.validation.BindingResult;

public interface ValidationService {
    BindingResult validateUser(UserDto userDto, BindingResult result, String objectName, boolean isRegister);

    BindingResult validateForgotPassword(UserDto userDto, BindingResult result);
}
