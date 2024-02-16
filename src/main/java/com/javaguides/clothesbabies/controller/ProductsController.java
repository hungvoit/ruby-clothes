package com.javaguides.clothesbabies.controller;

import com.javaguides.clothesbabies.common.URI;
import com.javaguides.clothesbabies.dto.ProductDto;
import com.javaguides.clothesbabies.model.Category;
import com.javaguides.clothesbabies.service.CategoryService;
import com.javaguides.clothesbabies.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;


@Controller
public class ProductsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    CategoryService categoryService;

    @Autowired
    PropertyService propertyService;

    @GetMapping(value= URI.ADMIN + URI.PRODUCTS)
    public String listProductAdmin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        // Get list parent category
        List<String> parentCategories = this.categoryService.getParentCategories();
        if (!CollectionUtils.isEmpty(parentCategories)) {
            parentCategories = parentCategories.stream().filter(parentCategory -> !parentCategory.equalsIgnoreCase("ROOT")).collect(Collectors.toList());
        }
        model.addAttribute("parentCategories", parentCategories);

        List<ProductDto> listProducts = new ArrayList<>();
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("product", new ProductDto());
        return URI.ADMIN + URI.PRODUCT + URI.LIST;
    }
}
