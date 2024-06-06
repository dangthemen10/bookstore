package com.phdang97.bookstore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SectionRequest {

    @NotNull(message = "Section name should not be null")
    @NotBlank(message = "Section name should not be blank")
    @Size(message = "Section name should not exceed 200 characters", max = 200)
    private String name;

    @Size(message = "Description should not exceed 500 characters", max = 500)
    private String description;
}
