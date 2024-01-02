package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.common.URI;
import com.javaguides.clothesbabies.dto.GlobalConfigurationDto;
import com.javaguides.clothesbabies.model.GlobalConfiguration;
import com.javaguides.clothesbabies.model.User;
import com.javaguides.clothesbabies.service.GlobalConfigurationService;
import com.javaguides.clothesbabies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
public class ModalDialogsController {
    @Autowired
    private GlobalConfigurationService globalConfigurationService;

    @Autowired
    private UserService userService;

    @GetMapping(value="/modal-add")
    public String modalAdd(Model model,
                           @RequestParam(value = "tabName", required = true) String tabName) {
        String urlAction = "";
        if (StringUtils.hasText(tabName)) {
            if (tabName.equalsIgnoreCase("global")) {
                model.addAttribute("initData", new GlobalConfigurationDto());
                urlAction = URI.GLOBAL + "/saveGlobal";
            }
        }
        return String.format("fragments/modal :: add-data(urlAction='%s', tabName='%s')",urlAction, tabName);
    }

    @GetMapping(value="/modal-edit/{id}")
    public String modalEdit(Model model,
                           @PathVariable("id") String id,
                           @RequestParam(value = "tabName", required = true) String tabName) {
        String urlAction = "";
        if (StringUtils.hasText(tabName)) {
            if (tabName.equalsIgnoreCase("global")) {
                GlobalConfiguration globalConfiguration = this.globalConfigurationService.findByCode(id);
                model.addAttribute("editData", globalConfiguration);
                urlAction = URI.GLOBAL + "/updGlobal";
            }
        }
        return String.format("fragments/modal :: edit-data(urlAction='%s', tabName='%s')",urlAction, tabName);
    }

    @GetMapping(value="/modal-del/{id}")
    public String modalDel(Model model,
                           @PathVariable("id") String id,
                           @RequestParam(value = "tabName", required = true) String tabName) {
        String urlAction = "";
        if (StringUtils.hasText(tabName)) {
            if (tabName.equalsIgnoreCase("global")) {
                GlobalConfiguration globalConfiguration = this.globalConfigurationService.findByCode(id);
                model.addAttribute("delData", globalConfiguration);
                urlAction = URI.GLOBAL + "/delGlobal";
            } else if (tabName.equalsIgnoreCase("user")) {
                User user = this.userService.findById(Long.valueOf(id));
                model.addAttribute("delData", user);
                urlAction = URI.USERS + "/delUser";
            }
        }
        return String.format("fragments/modal :: del-data(urlAction='%s', tabName='%s')",urlAction, tabName);
    }

    @GetMapping(value="/image-resize/{id}")
    public String modalResizeImage(Model model,
                           @RequestParam(value = "tabName", required = true) String tabName) {
        return "";
    }

    @GetMapping(value="/modal-reset-password/{id}")
    public String modalResetPassword(Model model, @PathVariable("id") String id) {
        User user = this.userService.findById(Long.valueOf(id));
        model.addAttribute("user", user);
        String urlAction = URI.USERS + "/resetPassword";
        return String.format("fragments/modal :: reset-password(urlAction='%s')",urlAction);
    }
}
