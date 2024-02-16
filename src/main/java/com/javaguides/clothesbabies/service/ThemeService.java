package com.javaguides.clothesbabies.service;

import com.javaguides.clothesbabies.dto.ThemeDto;

import java.util.List;

public interface ThemeService {
    ThemeDto getThemeByUserId(long userId);

    List<ThemeDto> getThemeList(int pageNumber, int size);

    void createTheme(ThemeDto themeDto);

    void updateTheme(ThemeDto themeDto);
}
