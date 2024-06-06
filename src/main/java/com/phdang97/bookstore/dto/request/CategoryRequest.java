package com.phdang97.bookstore.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotNull(message = "Category name should not be null")
    @NotBlank(message = "Category name should not be blank")
    @Size(message = "Description should not exceed 200 characters", max = 200)
    private String categoryName;

    @Size(message = "Description should not exceed 500 characters", max = 500)
    private String description;

    @NotNull(message = "Section index should not be null")
    @Min(value = 1, message = "Section index must be greater than or equal to 1")
    private Integer sectionIndex;
}
