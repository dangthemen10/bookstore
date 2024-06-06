package com.phdang97.bookstore.dto.response;

import com.phdang97.bookstore.dto.ItemDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartResponse {
  private List<ItemDTO> items;

  private Integer totalItems;

  private Double cartSubTotal;

  private Double cartShippingPrice;

  private Double total;

  private String couponCode;

  private Double totalAfterDiscount;
}
