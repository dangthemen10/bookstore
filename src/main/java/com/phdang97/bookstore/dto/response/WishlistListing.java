package com.phdang97.bookstore.dto.response;

import com.phdang97.bookstore.enums.Privacy;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WishlistListing {
  private Integer id;
  private String name;
  private LocalDateTime creationTime;
  private Privacy privacy;
}
