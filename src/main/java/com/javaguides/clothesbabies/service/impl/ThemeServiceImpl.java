package com.javaguides.clothesbabies.service.impl;

import com.javaguides.clothesbabies.dto.ThemeDto;
import com.javaguides.clothesbabies.model.Theme;
import com.javaguides.clothesbabies.repository.ThemeRepository;
import com.javaguides.clothesbabies.service.ThemeService;
import com.javaguides.clothesbabies.util.RestTemplateBuilderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class ThemeServiceImpl implements ThemeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeServiceImpl.class);

    @Autowired
    private ThemeRepository themeRepository;

    @Value("${unsplash.key}")
    private String key;

    @Override
    public ThemeDto getThemeByUserId(long userId) {
        Theme theme = themeRepository.findByUserId(userId);
        ThemeDto themeDto = null;
        if (Objects.nonNull(theme)) {
            themeDto = new ThemeDto();
            BeanUtils.copyProperties(theme, themeDto);
        }
        return themeDto;
    }

    @Override
    public List<ThemeDto> getThemeList(int pageNumber, int size) {
        List<ThemeDto> themeList = new ArrayList<>();
        try {
            final String API_URL = "https://api.unsplash.com/collections/317099/photos/?page=%d&per_page=%d&client_id=%s";
            String fullUrl = String.format(API_URL, pageNumber, size, key);
            try {
                List<?> map  =  RestTemplateBuilderUtil.restTemplateGetBuilderList(fullUrl);
                if (!CollectionUtils.isEmpty(map)) {
                    for (int i = 0; i < map.size(); i++) {
                        if (map.get(i) != null) {
                            Map<String, Object> obj = (Map<String, Object>) map.get(i);
                            if (obj.get("urls") != null) {
                                Map<String, Object> objUrlMap = (Map<String, Object>) obj.get("urls");
                                if (objUrlMap.get("regular") != null) {
                                    ThemeDto themeDto = new ThemeDto();
                                    themeDto.setImgUrl(objUrlMap.get("regular").toString());
                                    themeList.add(themeDto);
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex.getMessage());
            }
        } catch (Exception e) {
            throw new NoSuchElementException("Process interupted, not able to execute process");
        }
        return themeList;
    }

    @Override
    public void createTheme(ThemeDto request) {
        Theme theme = new Theme();
        BeanUtils.copyProperties(request, theme);
        theme.setCreateDate(new Date());
        theme.setUpdateDate(new Date());
        this.themeRepository.save(theme);
    }

    @Override
    public void updateTheme(ThemeDto request) {
        Theme theme = new Theme();
        BeanUtils.copyProperties(request, theme);
        theme.setUpdateDate(new Date());
        this.themeRepository.save(theme);
    }


}
