package com.phdang97.bookstore.service;

import com.phdang97.bookstore.dto.request.CategoryRequest;
import com.phdang97.bookstore.entity.Category;

import java.util.List;

public interface CategoryService {
    List<String> getAllCategories();

    Category getCategory(Integer categoryId);

    List<String> getCategoriesBySection(Integer sectionIndex);

    Category getCategoryByNameAndSectionName(Integer categoryId, Integer sectionId);

    Category addCategory(CategoryRequest request);

    Category updateCategory(Integer categoryId, CategoryRequest request);

    String deleteCategory(Integer categoryId);
}
