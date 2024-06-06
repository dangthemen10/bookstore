package com.phdang97.bookstore.controller;

import com.phdang97.bookstore.assembler.CategoryAssembler;
import com.phdang97.bookstore.dto.request.CategoryRequest;
import com.phdang97.bookstore.dto.response.CategoryResponse;
import com.phdang97.bookstore.entity.Category;
import com.phdang97.bookstore.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;

    @RequestMapping("/all")
    public List<String> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable("id") Integer categoryId) {
        Category category = categoryService.getCategory(categoryId);
        return new CategoryAssembler().toCategoryResponse(category);
    }

    @GetMapping("/section")
    public List<String> getCategoriesBySection(@RequestParam("sectionIndex") Integer sectionIndex) {
        return categoryService.getCategoriesBySection(sectionIndex);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@RequestBody @Valid CategoryRequest request) {
        Category category = categoryService.addCategory(request);
        return new CategoryAssembler().toCategoryResponse(category);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse updateCategory(@PathVariable("id") Integer categoryId, @RequestBody @Valid CategoryRequest request) {
        Category category = categoryService.updateCategory(categoryId, request);
        return new CategoryAssembler().toCategoryResponse(category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteCategory(@PathVariable("id") Integer categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
