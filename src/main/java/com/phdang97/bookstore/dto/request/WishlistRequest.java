package com.phdang97.bookstore.dto.request;

import com.phdang97.bookstore.enums.Privacy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WishlistRequest {
  @NotBlank private String name;

  private String description;
  @NotNull private Privacy privacy;

  private Integer bookId;
}
