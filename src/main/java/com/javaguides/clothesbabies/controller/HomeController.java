package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.dto.UserDto;
import com.javaguides.clothesbabies.model.User;
import com.javaguides.clothesbabies.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping(value = {"", "/", "/home"})
    public String displayHome(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "shop/home";
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = this.userService.findByEmail(email);
        UserDto userDto = new UserDto();
        if (user != null) {
            BeanUtils.copyProperties(user, userDto);
            String abbreviation = "";
            if (StringUtils.isNotEmpty(user.getFirstName())) {
                abbreviation += user.getFirstName().toUpperCase().charAt(0);
            }
            if (StringUtils.isNotEmpty(user.getLastName())) {
                abbreviation += user.getLastName().toUpperCase().charAt(0);
            }
            userDto.abbreviateName = abbreviation;
        }
        model.addAttribute("userDetails", userDto);
        return "shop/home";
    }
}
