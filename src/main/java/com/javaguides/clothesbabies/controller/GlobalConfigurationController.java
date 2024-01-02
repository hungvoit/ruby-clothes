package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.common.URI;
import com.javaguides.clothesbabies.dto.GlobalConfigurationDto;
import com.javaguides.clothesbabies.model.GlobalConfiguration;
import com.javaguides.clothesbabies.service.GlobalConfigurationService;
import com.javaguides.clothesbabies.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(URI.GLOBAL)
public class GlobalConfigurationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalConfigurationController.class);

    @Autowired
    GlobalConfigurationService globalConfigurationService;

    @Autowired
    PropertyService propertyService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String listGlobalConfig(Model model) {
        return findPaginated(1,  model);
    }

    @RequestMapping(value = "/page/{pageNo}", method = RequestMethod.GET)
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
        return "global" + URI.LIST;
    }

    @PostMapping("/saveGlobal")
    public String saveGlobal(@ModelAttribute(name = "initData") @Valid GlobalConfigurationDto globalConfigurationDto,
                             BindingResult result, RedirectAttributes redirectAttributes) {
        String redirectUrl = URI.REDIRECT + URI.GLOBAL + URI.LIST;
        if (result.hasErrors()) {
            return redirectUrl;
        }
        this.globalConfigurationService.createConfig(globalConfigurationDto);
        redirectAttributes.addFlashAttribute("message",
                this.propertyService.getMessage("message.successfully").replace("${content}", "created new global"));
        return redirectUrl;
    }

    @PostMapping("/updGlobal")
    public String updGlobal(@ModelAttribute(name = "editData") @Valid GlobalConfigurationDto globalConfigurationDto,
                             BindingResult result, RedirectAttributes redirectAttributes) {
        String redirectUrl = URI.REDIRECT + URI.GLOBAL + URI.LIST;
        if (result.hasErrors()) {
            return redirectUrl;
        }
        this.globalConfigurationService.updateConfig(globalConfigurationDto);
        redirectAttributes.addFlashAttribute("message",
        this.propertyService.getMessage("message.successfully").replace("${content}", "updated global"));
        return redirectUrl;
    }

    @PostMapping(value="/delGlobal/{id}")
    public String deleteGlobal(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        this.globalConfigurationService.deleteConfig(id);
        redirectAttributes.addFlashAttribute("message",
        this.propertyService.getMessage("message.successfully").replace("${content}", "deleted global"));
        return URI.REDIRECT + URI.GLOBAL + URI.LIST;
    }
}
