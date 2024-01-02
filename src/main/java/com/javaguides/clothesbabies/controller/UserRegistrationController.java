package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.dto.UserDto;
import com.javaguides.clothesbabies.service.UserService;
import com.javaguides.clothesbabies.service.ValidationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


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
    public UserDto userDto() {
        return new UserDto();
    }

    @GetMapping
    public String showRegistrationUserAccount() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute(name = "user") @Valid UserDto userDto,
            BindingResult result, RedirectAttributes redirectAttributes) {
        result = this.validationService.validateUser(userDto, result, "user", true);
        if (result.hasErrors()) {
            return "registration";
        }
        this.userService.createUser(userDto);
        Map<String, String> listOfRegisterMap = new HashMap<>();
        listOfRegisterMap.put("Email", userDto.getEmail());
        listOfRegisterMap.put("Phone", userDto.getPhone());
        listOfRegisterMap.put("Last Name", userDto.getLastName());
        listOfRegisterMap.put("First Name", userDto.getFirstName());
        TreeMap<String, String> sortMap = new TreeMap<>(listOfRegisterMap);
        redirectAttributes.addFlashAttribute("listOfRegisterMap", sortMap);
        return "redirect:/registration?success";
    }
}
