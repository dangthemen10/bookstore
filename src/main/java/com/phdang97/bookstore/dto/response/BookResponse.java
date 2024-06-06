package com.phdang97.bookstore.dto.response;

import com.phdang97.bookstore.entity.Category;
import com.phdang97.bookstore.entity.Section;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookResponse {
  private Integer id;

  private String title;

  private Double actualPrice;

  private Double discountPrice;

  private String bookThumbnail;

  private String description;

  private Integer numberOfPages;

  private String bookCover;

  private Integer copiesInStock;

  private Integer soldCopies;

  private Integer copiesToBePrinted;

  private LocalDateTime creationDate;

  private LocalDateTime updatedDate;

  private Category category;

  private Section section;
}
