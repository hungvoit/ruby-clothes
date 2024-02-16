package com.javaguides.clothesbabies.controller;

import com.google.gson.Gson;
import com.javaguides.clothesbabies.common.Rest;
import com.javaguides.clothesbabies.common.URI;
import com.javaguides.clothesbabies.dto.CategoryDto;
import com.javaguides.clothesbabies.model.Category;
import com.javaguides.clothesbabies.service.CategoryService;
import com.javaguides.clothesbabies.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CategoriesController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriesController.class);

    @Autowired
    CategoryService categoryService;

    @Autowired
    PropertyService propertyService;

    @GetMapping(value= URI.ADMIN + URI.CATEGORIES)
    public String listCategories(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return findPaginated(1,  model);
    }

    @GetMapping(value = URI.ADMIN + URI.CATEGORIES + "/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        List<Category> listCategories = null;
        Pageable paging = PageRequest.of(pageNo - 1, pageSize);
        Page<Category> pageCategory = this.categoryService.findAll(paging);
        if (pageCategory != null && pageCategory.getContent().size() > 0) {
            listCategories = pageCategory.getContent();
            model.addAttribute("totalPages", pageCategory.getTotalPages());
            model.addAttribute("totalItems", pageCategory.getTotalElements());
        }
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("category", new CategoryDto());
        return URI.ADMIN + URI.CATEGORY + URI.LIST;
    }

    @GetMapping(value = URI.ADMIN + URI.PARENT_CATEGORIES)
    public ResponseEntity<Rest> getParentCategories() {
        Rest rest = new Rest(HttpStatus.OK);
        List<String> parentCategories = this.categoryService.getParentCategories();
        rest.setData(parentCategories);
        return responseEntity(rest);
    }

    @GetMapping(value = URI.ADMIN + URI.CHILD_CATEGORIES + URI.PARENTCATEGORYCODE)
    public ResponseEntity<Rest> getChildCategoriesByParent(@PathVariable(value = "parentCategoryCode") String parentCategoryCode) {
        Rest rest = new Rest(HttpStatus.OK);
        List<String> childCategories = this.categoryService.getChildCategoriesByParent(parentCategoryCode);
        rest.setData(childCategories);
        return responseEntity(rest);
    }

    @GetMapping(value= URI.ADMIN + URI.CATEGORIES + "/code-exist")
    public ResponseEntity<Rest> checkExistCategoryCode(@RequestParam(required = true) String code) {
        boolean existed = this.categoryService.findByCategoryCode(code);
        Rest rest = new Rest(HttpStatus.OK);
        rest.setData(existed);
        return responseEntity(rest);
    }

    @GetMapping(value = URI.ADMIN + URI.CATEGORIES + "/findById" + URI.ID)
    public ResponseEntity<Rest> getCategoryById(@PathVariable(value = "id") Long id) {
        Rest rest = new Rest();
        try {
            Category category = this.categoryService.findById(id);
            rest.setHttpStatus(HttpStatus.OK);
            rest.setData(new Gson().toJson(category));
        } catch (Exception e) {
            rest.setHttpStatus(HttpStatus.NOT_FOUND);
            rest.setData(e.getMessage());
        }
        return responseEntity(rest);
    }

    @PostMapping(value = URI.API + URI.ADMIN + URI.CATEGORIES)
    public ResponseEntity<Rest> saveCategory(@Valid @RequestBody CategoryDto categoryDto) {
        Rest rest = new Rest();
        try {
            String content = "";
            if (categoryDto.getId() != null) {
                this.categoryService.updateCategory(categoryDto);
                content = "update category";
            } else {
                this.categoryService.createCategory(categoryDto);
                content = "create new category";
            }
            rest.setData(true);
            rest.setMessage(this.propertyService.getMessage("message.successfully").replace("${content}", content));
            rest.setHttpStatus(HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Error on save category", ex);
            rest.setData(false);
            rest.setMessage("Error on save category");
            rest.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return responseEntity(rest);
    }

    @PostMapping(value= URI.API + URI.ADMIN + URI.CATEGORIES + URI.ID)
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        this.categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("message",
                this.propertyService.getMessage("message.successfully").replace("${content}", "deleted category"));
        return URI.REDIRECT + URI.ADMIN + URI.CATEGORIES;
    }
}
