package com.javaguides.clothesbabies.repository;

import com.javaguides.clothesbabies.model.GlobalConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GlobalConfigurationRepository extends PagingAndSortingRepository<GlobalConfiguration, String> {
    GlobalConfiguration findByCode(String code);

    @Query("SELECT DISTINCT gl FROM GlobalConfiguration as gl WHERE (gl.code =:keyword OR gl.value LIKE :keyword OR (gl.description LIKE :keyword)) ORDER BY gl.code DESC")
    Page<GlobalConfiguration> findAllByKeyword(@Param("keyword") String keyword, Pageable page);
}
