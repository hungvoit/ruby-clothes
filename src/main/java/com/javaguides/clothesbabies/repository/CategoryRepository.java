package com.javaguides.clothesbabies.repository;

import com.javaguides.clothesbabies.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {

    Category findByCode(String code);

    List<Category> findAllByParentIdAndCodeNotContainingOrderByCodeAsc(String parentCategoryCode, String code);

}
