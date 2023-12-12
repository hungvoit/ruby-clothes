package com.javaguides.clothesbabies.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping
public class LoginController {
    @RequestMapping(value={"", "/", "login"})
    public String login(final Model model) {
        return "login";
    }
}
