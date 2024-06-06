package com.phdang97.bookstore.service;

import com.phdang97.bookstore.dto.request.SectionRequest;
import com.phdang97.bookstore.entity.Section;

import java.util.List;

public interface SectionService {
    Section getSectionById(Integer sectionId);

    List<String> getAllSections();

    Section addSection(SectionRequest request);

    Section updateSection(Integer sectionId, SectionRequest request);

    String deleteSection(Integer sectionId);
}
