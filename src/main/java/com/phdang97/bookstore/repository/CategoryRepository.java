package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findCategoryByIdAndSectionId(Integer categoryId, Integer sectionId);

    @Query("SELECT c.name FROM Category c WHERE c.section.id = :sectionIndex")
    List<String> findCategoryNamesBySectionIndex(Integer sectionIndex);

    @Query("SELECT c.name FROM Category c")
    List<String> getAllCategoryNames();

    Optional<Category> findByName(String categoryName);

}
