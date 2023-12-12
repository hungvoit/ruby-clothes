package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.dto.GlobalConfigurationDto;
import com.javaguides.clothesbabies.model.GlobalConfiguration;
import com.javaguides.clothesbabies.service.GlobalConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModalDialogsController {
    @Autowired
    private GlobalConfigurationService globalConfigurationService;

    @GetMapping(value="/modal-add")
    public String modalAdd(Model model,
                           @RequestParam(value = "tabName", required = true) String tabName) {
        String urlAction = "";
        if (StringUtils.hasText(tabName)) {
            if (tabName.equalsIgnoreCase("global")) {
                model.addAttribute("initData", new GlobalConfigurationDto());
                urlAction = "/global/saveGlobal";
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
                urlAction = "/global/updGlobal";
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
                urlAction = "/global/delGlobal";
            }
        }
        return String.format("fragments/modal :: del-data(urlAction='%s', tabName='%s')",urlAction, tabName);
    }

}
