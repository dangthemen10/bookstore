package com.phdang97.bookstore.dto.response;

import com.phdang97.bookstore.dto.ItemDTO;
import com.phdang97.bookstore.enums.Privacy;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WishlistResponse {
  private Integer wishlistId;
  private String name;
  private String description;
  private Privacy privacy;
  private List<ItemDTO> items;
}
