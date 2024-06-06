package com.phdang97.bookstore.controller;

import com.phdang97.bookstore.assembler.SectionAssembler;
import com.phdang97.bookstore.dto.request.SectionRequest;
import com.phdang97.bookstore.dto.response.SectionResponse;
import com.phdang97.bookstore.entity.Section;
import com.phdang97.bookstore.service.SectionService;
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
@RequestMapping("/api/sections")
public class SectionController {
    @Autowired
    private final SectionService sectionService;


    @GetMapping("/all")
    public List<String> getAllSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/{id}")
    public SectionResponse getSection(@PathVariable("id") Integer sectionId) {
        Section result = sectionService.getSectionById(sectionId);
        return new SectionAssembler().toSectionResponse(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SectionResponse addSection(@RequestBody @Valid SectionRequest request) {
        Section section = sectionService.addSection(request);
        return new SectionAssembler().toSectionResponse(section);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SectionResponse updateSection(@PathVariable(value = "id") Integer sectionId,
                                        @RequestBody @Valid SectionRequest request) {
        Section section = sectionService.updateSection(sectionId, request);
        return new SectionAssembler().toSectionResponse(section);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteSection(@PathVariable(value = "id") Integer sectionId) {
        return sectionService.deleteSection(sectionId);

    }
}
