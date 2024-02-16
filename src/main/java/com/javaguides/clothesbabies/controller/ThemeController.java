package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.common.Rest;
import com.javaguides.clothesbabies.common.URI;
import com.javaguides.clothesbabies.dto.ThemeDto;
import com.javaguides.clothesbabies.service.PropertyService;
import com.javaguides.clothesbabies.service.ThemeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ThemeController extends BaseController {

    @Autowired
    ThemeService themeService;

    @Autowired
    PropertyService propertyService;

    @GetMapping(value = URI.ADMIN + URI.THEME + "/page/{pageNo}")
    public ResponseEntity<Rest> findPaginated(@PathVariable(value = "pageNo") int pageNo) {
        Rest rest = new Rest();
        try {
            int pageSize = 20;
            List<ThemeDto> lstTheme = this.themeService.getThemeList(pageNo, pageSize);
            rest.setData(lstTheme);
            rest.setHttpStatus(HttpStatus.OK);
        } catch (Exception ex) {
            rest.setMessage("Error on get list theme");
            rest.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return responseEntity(rest);
    }

    @GetMapping(value = URI.ADMIN + URI.THEME + "/{userId}")
    public ResponseEntity<Rest> findThemeByUserId(@PathVariable(value = "userId") long userId) {
        Rest rest = new Rest();
        try {
            ThemeDto themeDto= this.themeService.getThemeByUserId(userId);
            rest.setData(themeDto);
            rest.setHttpStatus(HttpStatus.OK);
        } catch (Exception ex) {
            rest.setMessage("Error on get theme by userId");
            rest.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return responseEntity(rest);
    }

    @PostMapping(value = URI.API + URI.ADMIN + URI.THEME)
    public ResponseEntity<Rest> saveTheme(@RequestBody ThemeDto request) {
        Rest rest = new Rest();
        try {
            String content = "";
            ThemeDto themeDto = this.themeService.getThemeByUserId(request.getUserId());
            if (themeDto != null) {
                themeDto.setImgUrl(request.getImgUrl());
                this.themeService.updateTheme(themeDto);
                content = "update theme";
            } else {
                this.themeService.createTheme(request);
                content = "create theme";
            }
            rest.setData(true);
            rest.setMessage(this.propertyService.getMessage("message.successfully").replace("${content}", content));
            rest.setHttpStatus(HttpStatus.OK);
        } catch (Exception ex) {
            rest.setData(false);
            rest.setMessage("Error on save theme");
            rest.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return responseEntity(rest);
    }
}
