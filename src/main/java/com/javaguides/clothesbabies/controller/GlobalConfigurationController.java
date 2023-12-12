package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.dto.GlobalConfigurationDto;
import com.javaguides.clothesbabies.model.GlobalConfiguration;
import com.javaguides.clothesbabies.service.GlobalConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/global")
public class GlobalConfigurationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalConfigurationController.class);

    @Autowired
    GlobalConfigurationService globalConfigurationService;

    @ModelAttribute(name = "global")
    public GlobalConfigurationDto globalConfigurationDto() {
        return new GlobalConfigurationDto();
    }

    @RequestMapping(value={"/","/list"}, method = RequestMethod.GET)
    public String listGlobalConfig(Model model) {
        return findPaginated(1,  model);
    }

    @RequestMapping(value = "/list/page/{pageNo}", method = RequestMethod.GET)
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        List<GlobalConfiguration> lstGlobalConfig = null;
        Page<GlobalConfiguration> pageGlobalConfig = this.globalConfigurationService.getConfigByList(pageNo, pageSize);
        if (pageGlobalConfig != null && pageGlobalConfig.getContent().size() > 0) {
            lstGlobalConfig = pageGlobalConfig.getContent();
            model.addAttribute("totalPages", pageGlobalConfig.getTotalPages());
            model.addAttribute("totalItems", pageGlobalConfig.getTotalElements());
        }
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("listGlobal", lstGlobalConfig);
        return "global/list";
    }

    @RequestMapping(value = "/findByCode/{code}", method = RequestMethod.GET)
    public GlobalConfiguration findByCode(@PathVariable(value = "code") String code, Model model) {
        GlobalConfiguration globalConfiguration = this.globalConfigurationService.findByCode(code);
        model.addAttribute("global", globalConfiguration);
        return globalConfiguration;
    }

    @PostMapping("/saveGlobal")
    public String saveGlobal(@ModelAttribute(name = "global") @Valid GlobalConfigurationDto globalConfigurationDto, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/global/list";
        }
        this.globalConfigurationService.createConfig(globalConfigurationDto);
        return "redirect:/global/list?success";
    }
}
