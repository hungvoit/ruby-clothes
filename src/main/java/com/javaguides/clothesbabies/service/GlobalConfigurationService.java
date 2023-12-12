package com.javaguides.clothesbabies.service;

import com.javaguides.clothesbabies.dto.GlobalConfigurationDto;
import com.javaguides.clothesbabies.model.GlobalConfiguration;
import org.springframework.data.domain.Page;

public interface GlobalConfigurationService {

    Page<GlobalConfiguration> getConfigByList(int pageNumber, int size);

    GlobalConfiguration findByCode(String code);

    void createConfig(GlobalConfigurationDto request);

    void updateConfig(GlobalConfigurationDto request);

    void deleteConfig(String code);
}
