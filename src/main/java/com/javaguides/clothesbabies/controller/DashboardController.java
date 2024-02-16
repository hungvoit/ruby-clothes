package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.model.User;
import com.javaguides.clothesbabies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    UserService userService;

    @GetMapping
    public String displayDashboard(HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String email = userDetail.getUsername();
        User user = this.userService.findByEmail(email);
        if (user != null) {
            request.getSession().setAttribute("userId", user.getId());
        }
        return "dashboard";
    }
}
