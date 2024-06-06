package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.dto.request.SectionRequest;
import com.phdang97.bookstore.entity.Section;
import com.phdang97.bookstore.repository.SectionRepository;
import com.phdang97.bookstore.service.SectionService;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SectionServiceImpl implements SectionService {
  @Autowired private final SectionRepository sectionRepository;

  @Override
  public List<String> getAllSections() {
    return sectionRepository.getAllSectionsNames();
  }

  @Override
  public Section getSectionById(Integer sectionId) {
    Optional<Section> checkSection = sectionRepository.findById(sectionId);
    return checkSection.orElseThrow(
        () ->
            new EntityNotFoundException(
                String.format("The section with id %d is not found", sectionId)));
  }

  @Override
  public Section addSection(SectionRequest request) {
    String sectionName = request.getName();
    String sectionDescription = request.getDescription();
    validateSectionDuplicate(sectionName);
    Section section = Section.builder().name(sectionName).description(sectionDescription).build();
    Section newSection = sectionRepository.save(section);
    log.info("A section with name {} is added", sectionName);
    return newSection;
  }

  @Override
  public Section updateSection(Integer sectionId, SectionRequest request) {
    Section section = getSectionById(sectionId);
    validateSectionDuplicate(request.getName());
    section.setName(request.getName());
    section.setDescription(request.getDescription());
    Section newSection = sectionRepository.save(section);
    log.info("A section with id {} has been updated!", sectionId);
    return newSection;
  }

  @Override
  public String deleteSection(Integer sectionId) {
    Section section = getSectionById(sectionId);
    sectionRepository.delete(section);
    log.info("A section with id {} has been deleted", sectionId);
    return String.format("A section with id %d has been deleted!", sectionId);
  }

  private void validateSectionDuplicate(String sectionName) {
    Optional<Section> isPresent = sectionRepository.findByName(sectionName);
    if (isPresent.isPresent()) {
      throw new EntityExistsException(
          String.format("A section with id %s is already exist", sectionName));
    }
  }
}
