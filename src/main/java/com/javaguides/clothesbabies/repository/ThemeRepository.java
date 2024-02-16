package com.javaguides.clothesbabies.repository;

import com.javaguides.clothesbabies.model.Theme;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ThemeRepository extends PagingAndSortingRepository<Theme, Long> {
    Theme findByUserId(long userId);
}
