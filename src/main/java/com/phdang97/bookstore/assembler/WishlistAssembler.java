package com.phdang97.bookstore.assembler;

import com.phdang97.bookstore.dto.ItemDTO;
import com.phdang97.bookstore.dto.response.WishlistResponse;
import com.phdang97.bookstore.entity.Wishlist;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WishlistAssembler {

  public WishlistResponse toWishlistResponse(Wishlist wishlist, List<ItemDTO> items) {
    return WishlistResponse.builder()
        .wishlistId(wishlist.getId())
        .name(wishlist.getName())
        .description(wishlist.getDescription())
        .privacy(wishlist.getPrivacy())
        .items(items)
        .build();
  }
}
