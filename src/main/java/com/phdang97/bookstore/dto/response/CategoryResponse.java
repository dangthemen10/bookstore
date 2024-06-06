package com.phdang97.bookstore.dto.response;

import com.phdang97.bookstore.entity.Book;
import com.phdang97.bookstore.entity.Section;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Integer id;
    private String name;
    private String description;
    private Section section;
    private Set<Book> books;
    private LocalDateTime creationTime;
    private LocalDateTime lastUpdatedTime;
    private String createdBy;
    private  String updatedBy;
}
