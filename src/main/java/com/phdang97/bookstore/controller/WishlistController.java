package com.phdang97.bookstore.controller;

import com.phdang97.bookstore.dto.request.AddItemRequest;
import com.phdang97.bookstore.dto.request.WishlistRequest;
import com.phdang97.bookstore.dto.response.WishlistListing;
import com.phdang97.bookstore.dto.response.WishlistResponse;
import com.phdang97.bookstore.service.WishlistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishlistController {
  @Autowired private final WishlistService wishlistService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public WishlistResponse createWishlist(@RequestBody WishlistRequest createWishlistRequest) {
    return wishlistService.createWishlist(createWishlistRequest);
  }

  @PostMapping("/{wishlist-id}/item")
  public WishlistResponse addItem(
      @PathVariable(name = "wishlist-id") Integer wishlistId, @RequestBody AddItemRequest request) {
    return wishlistService.addItem(wishlistId, request);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("{wishlist-id}/item/{item-id}")
  public String removeItem(
      @PathVariable(name = "wishlist-id") Integer wishlistId,
      @PathVariable(name = "item-id") Integer itemId) {
    return wishlistService.removeItem(wishlistId, itemId);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{wishlist-id}")
  public String deleteWishlist(@PathVariable(name = "wishlist-id") Integer wishlistId) {
    return wishlistService.deleteWishlist(wishlistId);
  }

  @PutMapping("/{wishlist-id}")
  public WishlistResponse updateWishlist(
      @PathVariable(name = "wishlist-id") Integer wishlistId,
      @RequestBody WishlistRequest createWishlistRequest) {
    return wishlistService.updateWishlist(wishlistId, createWishlistRequest);
  }

  @GetMapping("/{wishlist-id}")
  public WishlistResponse getWishlist(@PathVariable(name = "wishlist-id") Integer wishlistId) {
    return wishlistService.getWishlist(wishlistId);
  }

  @GetMapping
  public List<WishlistListing> getWishlists() {
    return wishlistService.getWishlists();
  }
}
