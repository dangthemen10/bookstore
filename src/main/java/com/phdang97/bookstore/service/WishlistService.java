package com.phdang97.bookstore.service;

import com.phdang97.bookstore.dto.request.AddItemRequest;
import com.phdang97.bookstore.dto.request.WishlistRequest;
import com.phdang97.bookstore.dto.response.WishlistListing;
import com.phdang97.bookstore.dto.response.WishlistResponse;
import java.util.List;

public interface WishlistService {
  WishlistResponse createWishlist(WishlistRequest createWishlistRequest);

  WishlistResponse addItem(Integer wishlistId, AddItemRequest request);

  String removeItem(Integer wishlistId, Integer itemId);

  String deleteWishlist(Integer wishlistId);

  WishlistResponse updateWishlist(Integer wishlistId, WishlistRequest request);

  WishlistResponse getWishlist(Integer wishlistId);

    List<WishlistListing> getWishlists();
}
