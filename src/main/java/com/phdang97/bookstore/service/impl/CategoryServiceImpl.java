package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.dto.request.CategoryRequest;
import com.phdang97.bookstore.entity.Category;
import com.phdang97.bookstore.entity.Section;
import com.phdang97.bookstore.repository.CategoryRepository;
import com.phdang97.bookstore.service.CategoryService;
import com.phdang97.bookstore.service.SectionService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
  @Autowired private final CategoryRepository categoryRepository;

  @Autowired private final SectionService sectionService;

  /**
   * Retrieves a list of all categories in the system.
   *
   * @return a list of category names
   */
  @Override
  public List<String> getAllCategories() {
    return categoryRepository.getAllCategoryNames();
  }

  /**
   * Retrieves a category by its id.
   *
   * @param categoryId the id of the category to be retrieved
   * @return the category object if found, otherwise throws an EntityNotFoundException
   */
  @Override
  public Category getCategory(Integer categoryId) {
    Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
    return categoryOptional.orElseThrow(
        () ->
            new EntityNotFoundException(
                String.format("The category with name %s is not found", categoryId)));
  }

  /**
   * Retrieves a list of category names belonging to a specific section.
   *
   * @param sectionIndex the index of the section to retrieve categories from
   * @return a list of category names in the specified section
   */
  @Override
  public List<String> getCategoriesBySection(Integer sectionIndex) {
    return categoryRepository.findCategoryNamesBySectionIndex(sectionIndex);
  }

  /**
   * Retrieves a category by its name and the section it belongs to.
   *
   * @param categoryId the id of the category
   * @param sectionId the id of the section
   * @return the category object if found, otherwise throws an EntityNotFoundException
   */
  @Override
  public Category getCategoryByNameAndSectionName(Integer categoryId, Integer sectionId) {
    return categoryRepository
        .findCategoryByIdAndSectionId(categoryId, sectionId)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    String.format(
                        "there is no category with name %s in the section with name %s",
                        categoryId, sectionId)));
  }

  /**
   * Adds a new category to the system.
   *
   * @param request the request containing the details of the new category
   * @return the newly added category object
   * @throws EntityExistsException if a category with the same name already exists
   */
  @Override
  public Category addCategory(CategoryRequest request) {
    String categoryName = request.getCategoryName();
    String description = request.getDescription();
    Integer sectionIndex = request.getSectionIndex();
    validateCategoryDuplicate(categoryName);
    Section section = sectionService.getSectionById(sectionIndex);
    Category category =
        Category.builder().name(categoryName).description(description).section(section).build();
    Category result = categoryRepository.save(category);
    log.info("A category with name {} is added", categoryName);
    return result;
  }

  /**
   * Updates a category in the system.
   *
   * @param categoryId the id of the category to be updated
   * @param request the request containing the updated category details
   * @return the updated category object
   * @throws EntityNotFoundException if the category with the given id is not found
   */
  @Override
  public Category updateCategory(Integer categoryId, CategoryRequest request) {
    Category category = getCategory(categoryId);
    Section section = sectionService.getSectionById(request.getSectionIndex());
    validateCategoryDuplicate(request.getCategoryName());
    category.setName(request.getCategoryName());
    category.setDescription(request.getDescription());
    category.setSection(section);
    Category result = categoryRepository.save(category);
    log.info("A category with name {} has been updated", request.getCategoryName());
    return result;
  }

  /**
   * Deletes a category from the system.
   *
   * @param categoryId the id of the category to be deleted
   * @return a string message indicating the successful deletion of the category
   * @throws EntityNotFoundException if the category with the given id is not found
   */
  @Override
  public String deleteCategory(Integer categoryId) {
    Category category = getCategory(categoryId);
    categoryRepository.delete(category);
    log.info("A category with id {} has been deleted", categoryId);
    return String.format("A category with id %d has been deleted!", categoryId);
  }

  /**
   * Validates if a category with the same name already exists in the system.
   *
   * @param categoryName the name of the category to be validated
   * @throws EntityExistsException if a category with the same name already exists
   */
  private void validateCategoryDuplicate(String categoryName) {
    Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
    if (optionalCategory.isPresent()) {
      throw new EntityExistsException(
          String.format("Category with name '%s' is already in the system.", categoryName));
    }
  }
}
