package com.javaguides.clothesbabies.service.impl;

import com.javaguides.clothesbabies.dto.GlobalConfigurationDto;
import com.javaguides.clothesbabies.model.GlobalConfiguration;
import com.javaguides.clothesbabies.repository.GlobalConfigurationRepository;
import com.javaguides.clothesbabies.service.GlobalConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class GlobalConfigurationServiceImpl implements GlobalConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalConfigurationServiceImpl.class);

    @Autowired
    GlobalConfigurationRepository globalConfigurationRepository;

    @Override
    public Page<GlobalConfiguration> getConfigByList(int pageNumber, int size){
        Pageable paging = PageRequest.of(pageNumber - 1, size);
        return this.globalConfigurationRepository.findAll(paging);
    }

    @Override
    public GlobalConfiguration findByCode(String code) {
        return this.globalConfigurationRepository.findByCode(code);
    }

    @Override
    public void createConfig(GlobalConfigurationDto request) {
        GlobalConfiguration config = this.globalConfigurationRepository.findByCode(request.getCode());
        if (Objects.nonNull(config)){
            throw new NoSuchElementException("Config with key: " + request.getCode() + " already exist");
        }
        config = new GlobalConfiguration();
        BeanUtils.copyProperties(request, config);
        config.setCreateDate(new Date());
        config.setUpdateDate(new Date());
        this.globalConfigurationRepository.save(config);
    }

    @Override
    public void updateConfig(GlobalConfigurationDto request) {
        GlobalConfiguration config = this.globalConfigurationRepository.findByCode(request.getCode());
        if (Objects.isNull(config)){
            throw new NoSuchElementException("Config with key: " + request.getCode() + " existn't");
        }
        config.setValue(request.getValue());
        config.setDescription(request.getDescription());
        config.setUpdateDate(new Date());
        this.globalConfigurationRepository.save(config);
    }


    @Override
    public void deleteConfig(String code) {
        GlobalConfiguration config = this.globalConfigurationRepository.findByCode(code);
        if (Objects.isNull(config)){
            throw new NoSuchElementException("Config with key: " + code + " existn't");
        }
        this.globalConfigurationRepository.delete(config);
    }

    public Map<String, String> getConfigByListCode(List<String> codes) {
        Map<String, String> resultMap = new HashMap<>();
        if (codes != null && codes.size() > 0) {
            codes.forEach(code -> {
                GlobalConfiguration configuration = this.globalConfigurationRepository.findByCode(code);
                resultMap.put(code, configuration.getValue());
            });
        }
        return resultMap;
    }

    public String getConfigByKeys(String key) {
        List<String> codes = new ArrayList<>();
        codes.add(key);
        Map<String, String> mapConfig = this.getConfigByListCode(codes);
        return mapConfig.get(key);
    }

}
