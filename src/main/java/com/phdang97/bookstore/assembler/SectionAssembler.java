package com.phdang97.bookstore.assembler;

import com.phdang97.bookstore.dto.response.SectionResponse;
import com.phdang97.bookstore.entity.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionAssembler {
    public SectionResponse toSectionResponse(Section section) {
        return SectionResponse.builder()
               .id(section.getId())
               .name(section.getName())
               .description(section.getDescription())
               .creationTime(section.getCreationTime())
               .lastUpdatedTime(section.getLastUpdatedTime())
               .createdBy(section.getCreatedBy())
               .updatedBy(section.getUpdatedBy())
               .build();
    }
}
