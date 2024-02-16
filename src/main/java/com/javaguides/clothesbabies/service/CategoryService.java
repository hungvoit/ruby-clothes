package com.javaguides.clothesbabies.service;

import com.javaguides.clothesbabies.dto.CategoryDto;
import com.javaguides.clothesbabies.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable);

    List<String> getChildCategoriesByParent(String parentCategoryCode);

    List<String> getParentCategories();

    boolean findByCategoryCode(String code);

    Category findById(Long id);

    void createCategory(CategoryDto request);

    void updateCategory(CategoryDto request);

    void deleteCategory(Long id);
}
