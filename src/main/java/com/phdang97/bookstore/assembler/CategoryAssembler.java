package com.phdang97.bookstore.assembler;

import com.phdang97.bookstore.dto.response.CategoryResponse;
import com.phdang97.bookstore.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryAssembler {
  public CategoryResponse toCategoryResponse(Category category) {
    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .description(category.getDescription())
        .section(category.getSection())
        .books(category.getBooks())
        .creationTime(category.getCreationTime())
        .lastUpdatedTime(category.getLastUpdatedTime())
        .createdBy(category.getCreatedBy())
        .updatedBy(category.getUpdatedBy())
        .build();
  }

  public Category toCategory(CategoryResponse category) {
    return Category.builder()
        .id(category.getId())
        .name(category.getName())
        .description(category.getDescription())
        .section(category.getSection())
        .books(category.getBooks())
        .build();
  }
}
