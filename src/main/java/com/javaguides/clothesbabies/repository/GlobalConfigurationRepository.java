package com.javaguides.clothesbabies.repository;

import com.javaguides.clothesbabies.model.GlobalConfiguration;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface GlobalConfigurationRepository extends PagingAndSortingRepository<GlobalConfiguration, String> {
    GlobalConfiguration findByCode(String code);
}
