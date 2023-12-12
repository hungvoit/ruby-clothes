package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.dto.UserRegistrationDto;
import com.javaguides.clothesbabies.service.UserService;
import com.javaguides.clothesbabies.service.ValidationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;

    private ValidationService validationService;

    public UserRegistrationController(UserService userService,
        ValidationService validationService) {
        super();
        this.userService = userService;
        this.validationService = validationService;
    }

    @ModelAttribute(name = "user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationUserAccount() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute(name = "user") @Valid UserRegistrationDto registrationDto, BindingResult result) {
        result = this.validationService.validateUser(registrationDto, result);
        if (result.hasErrors()) {
            return "registration";
        }

        this.userService.saveUser(registrationDto);
        return "redirect:/registration?success";
    }
}
