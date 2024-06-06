package com.phdang97.bookstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionResponse {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime creationTime;
    private LocalDateTime lastUpdatedTime;
    private String createdBy;
    private  String updatedBy;
}
