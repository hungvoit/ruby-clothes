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

    @PostMapping("/saveGlobal")
    public String saveGlobal(@ModelAttribute(name = "initData") @Valid GlobalConfigurationDto globalConfigurationDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/global/list";
        }
        this.globalConfigurationService.createConfig(globalConfigurationDto);
        model.addAttribute("message", "You're successfully create new configuration.");
        return "redirect:/global/list";
    }

    @PostMapping("/updGlobal")
    public String updGlobal(@ModelAttribute(name = "editData") @Valid GlobalConfigurationDto globalConfigurationDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/global/list";
        }
        this.globalConfigurationService.updateConfig(globalConfigurationDto);
        model.addAttribute("message", "You're successfully update configuration.");
        return "redirect:/global/list";
    }

    @PostMapping(value="/delGlobal/{id}")
    public String deleteGlobal(@PathVariable("id") String id, Model model) {
        this.globalConfigurationService.deleteConfig(id);
        model.addAttribute("message", "You're successfully delete configuration.");
        return "redirect:/global/list";
    }
}
