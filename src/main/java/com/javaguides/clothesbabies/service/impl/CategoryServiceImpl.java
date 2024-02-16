package com.javaguides.clothesbabies.service.impl;

import com.javaguides.clothesbabies.dto.CategoryDto;
import com.javaguides.clothesbabies.model.Category;
import com.javaguides.clothesbabies.repository.CategoryRepository;
import com.javaguides.clothesbabies.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        Page<Category> categories = null;
        try {
            categories = this.categoryRepository.findAll(pageable);
        } catch (Exception e) {
            throw new NoSuchElementException("Process interupted, not able to execute process");
        }
        return categories;
    }

    @Override
    public List<String> getChildCategoriesByParent(String parentCategoryCode) {
        List<Category> categories = this.categoryRepository.findAllByParentIdAndCodeNotContainingOrderByCodeAsc(parentCategoryCode, "ROOT");
        if (!CollectionUtils.isEmpty(categories)) {
            return categories.stream().map(Category::getCode).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<String> getParentCategories() {
        List<String> parentCategories = Collections.singletonList("ROOT");
        PageRequest pageRequest = PageRequest.of(0, 999999999);
        Page<Category> pageCategory = this.categoryRepository.findAll(pageRequest);
        if (pageCategory != null && pageCategory.getContent().size() > 0) {
            List<Category> listCategories = pageCategory.getContent();
            if (!CollectionUtils.isEmpty(listCategories)) {
                parentCategories = listCategories.stream().filter(category -> "ROOT".equalsIgnoreCase(category.getParentId())).map(Category::getCode).distinct().collect(Collectors.toList());
            }
        }
        return parentCategories;
    }

    @Override
    public boolean findByCategoryCode(String code) {
        Category category = this.categoryRepository.findByCode(code);
        return Objects.nonNull(category);
    }

    @Override
    public Category findById(Long id) {
        Category category = new Category();
        Optional<Category> categoryOpt = this.categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
        }
        return category;
    }

    @Override
    public void createCategory(CategoryDto request) {
        Category category = this.categoryRepository.findByCode(request.getCode());
        if (Objects.nonNull(category)){
            throw new NoSuchElementException("Category with code: " + request.getCode() + " already exist");
        }
        category = new Category();
        BeanUtils.copyProperties(request, category);
        category.setCreateDate(new Date());
        category.setUpdateDate(new Date());
        this.categoryRepository.save(category);
    }

    @Override
    public void updateCategory(CategoryDto request) {
        Optional<Category> categoryOpt = this.categoryRepository.findById(request.getId());
        if (!categoryOpt.isPresent()){
            throw new NoSuchElementException("Category with code: " + request.getCode() + " existn't");
        } else {
            Category category = categoryOpt.get();
            if (StringUtils.isNotEmpty(request.getCode())) category.setCode(request.getCode());
            if (StringUtils.isNotEmpty(request.getName())) category.setName(request.getName());
            if (StringUtils.isNotEmpty(request.getParentId())) category.setParentId(request.getParentId());
            category.setUpdateDate(new Date());
            this.categoryRepository.save(category);
        }
    }

    @Override
    public void deleteCategory(Long id) {
        Optional<Category> categoryOtp = this.categoryRepository.findById(id);
        if (!categoryOtp.isPresent()){
            throw new NoSuchElementException("Category with id: " + id + " existn't");
        }
        Category category = categoryOtp.get();
        this.categoryRepository.delete(category);
    }


}
