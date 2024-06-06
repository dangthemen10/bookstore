package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.entity.Section;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
    @Query("SELECT s.name FROM Section s")
    List<String> getAllSectionsNames();

    Optional<Section> findByName(String sectionName);
}
