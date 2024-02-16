package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.common.Rest;
import com.javaguides.clothesbabies.common.URI;
import com.javaguides.clothesbabies.dto.GlobalConfigurationDto;
import com.javaguides.clothesbabies.model.GlobalConfiguration;
import com.javaguides.clothesbabies.service.GlobalConfigurationService;
import com.javaguides.clothesbabies.service.PropertyService;
import com.javaguides.clothesbabies.service.ValidationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class GlobalConfigurationController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalConfigurationController.class);

    @Autowired
    GlobalConfigurationService globalConfigurationService;

    @Autowired
    PropertyService propertyService;

    @Autowired
    ValidationService validationService;

    @GetMapping(value=URI.ADMIN + URI.GLOBAL)
    public String listGlobalConfig(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return findPaginated(1,  model);
    }

    @GetMapping(value = URI.ADMIN + URI.GLOBAL + "/page/{pageNo}")
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
        return URI.ADMIN + URI.GLOBAL + URI.LIST;
    }

    @GetMapping(value = URI.ADMIN + URI.GLOBAL + "/showGlobalForm")
    public String showGlobalForm(@RequestParam(required = false, defaultValue = "") String code, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        GlobalConfigurationDto globalConfigurationDto =  new GlobalConfigurationDto();
        if (StringUtils.isNotBlank(code)) {
            GlobalConfiguration globalConfiguration = this.globalConfigurationService.findByCode(code);
            BeanUtils.copyProperties(globalConfiguration, globalConfigurationDto);
            globalConfigurationDto.setFlagChange(true);
        }
        model.addAttribute("global", globalConfigurationDto);
        return URI.ADMIN + URI.GLOBAL + "/create";
    }

    @PostMapping(value = URI.API + URI.ADMIN + URI.GLOBAL)
    public ResponseEntity<Rest> saveGlobal(@Valid @RequestBody GlobalConfigurationDto globalConfigurationDto) {
        Rest rest = new Rest();
        try {
            String content = "";
            if (globalConfigurationDto.getFlagChange()) {
                this.globalConfigurationService.updateConfig(globalConfigurationDto);
                content = "update global";
            } else {
                this.globalConfigurationService.createConfig(globalConfigurationDto);
                content = "create new global";
            }
            rest.setData(true);
            rest.setMessage(this.propertyService.getMessage("message.successfully").replace("${content}", content));
            rest.setHttpStatus(HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Error on save global config", ex);
            rest.setData(false);
            rest.setMessage("Error on save global config");
            rest.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return responseEntity(rest);
    }

    @PostMapping(value= URI.API + URI.ADMIN + URI.GLOBAL + URI.ID)
    public String deleteGlobal(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        this.globalConfigurationService.deleteConfig(id);
        redirectAttributes.addFlashAttribute("message",
        this.propertyService.getMessage("message.successfully").replace("${content}", "deleted global"));
        return URI.REDIRECT + URI.ADMIN + URI.GLOBAL;
    }
}
