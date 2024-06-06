package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.assembler.WishlistAssembler;
import com.phdang97.bookstore.dto.ItemDTO;
import com.phdang97.bookstore.dto.request.AddItemRequest;
import com.phdang97.bookstore.dto.request.WishlistRequest;
import com.phdang97.bookstore.dto.response.WishlistListing;
import com.phdang97.bookstore.dto.response.WishlistResponse;
import com.phdang97.bookstore.entity.Book;
import com.phdang97.bookstore.entity.Wishlist;
import com.phdang97.bookstore.entity.WishlistItem;
import com.phdang97.bookstore.enums.Privacy;
import com.phdang97.bookstore.repository.WishlistItemRepository;
import com.phdang97.bookstore.repository.WishlistRepository;
import com.phdang97.bookstore.service.BookService;
import com.phdang97.bookstore.service.WishlistService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {
  @Autowired private final WishlistRepository wishlistRepository;

  @Autowired private final BookService bookService;

  @Autowired private final UserService userService;

  @Autowired private final WishlistItemRepository wishlistItemRepository;

  public WishlistResponse createWishlist(WishlistRequest createWishlistRequest) {
    checkIfWishlistExists(createWishlistRequest.getName(), userService.userId());
    Wishlist wishlist =
        Wishlist.builder()
            .name(createWishlistRequest.getName())
            .description(createWishlistRequest.getDescription())
            .privacy(createWishlistRequest.getPrivacy())
            .user(userService.getUserById(userService.userId()))
            .build();
    wishlist = wishlistRepository.save(wishlist);
    if (createWishlistRequest.getBookId() != null) {
      Book book = bookService.getBookById(createWishlistRequest.getBookId());
      WishlistItem wishlistItem =
          WishlistItem.builder().book(book).quantity(1).wishlist(wishlist).build();
      wishlistItemRepository.save(wishlistItem);
    }
    return buildWishlistResponse(wishlist);
  }

  public void checkIfWishlistExists(String name, Integer userId) {
    if (wishlistRepository.existsByNameAndUserId(name, userId)) {
      throw new EntityExistsException("Wishlist with name " + name + " already exists");
    }
  }

  public WishlistResponse buildWishlistResponse(Wishlist wishlist) {
    List<ItemDTO> items = wishlistItemRepository.getWishlistItems(wishlist);
    return new WishlistAssembler().toWishlistResponse(wishlist, items);
  }

  @Override
  public WishlistResponse addItem(Integer wishlistId, AddItemRequest request) {
    Wishlist wishlist = getWishlistByIdAndUserId(wishlistId, userService.userId());
    Book book = bookService.getBookById(request.getBookId());
    if (wishlistItemRepository.existsByWishlistAndBook(wishlist, book)) {
      wishlistItemRepository.updateWishlistItemQuantity(wishlist, book, request.getQuantity());
    } else {
      WishlistItem wishlistItem =
          WishlistItem.builder()
              .book(book)
              .quantity(request.getQuantity())
              .wishlist(wishlist)
              .build();
      wishlistItemRepository.save(wishlistItem);
    }
    return buildWishlistResponse(wishlist);
  }

  public Wishlist getWishlistByIdAndUserId(Integer wishlistId, Integer userId) {
    return wishlistRepository
        .findByIdAndUserId(wishlistId, userId)
        .orElseThrow(
            () -> new EntityNotFoundException("Wishlist with id " + wishlistId + " not found"));
  }

  @Override
  public String removeItem(Integer wishlistId, Integer itemId) {
    Wishlist wishlist = getWishlistByIdAndUserId(wishlistId, userService.userId());
    if (!wishlistItemRepository.existsByWishlistAndId(wishlist, itemId)) {
      throw new EntityNotFoundException(
          "Item with id " + itemId + " not found in wishlist with id " + wishlistId);
    }
    wishlistItemRepository.deleteById(itemId);
    return "Item with id " + itemId + " removed from wishlist with id " + wishlistId;
  }

  @Override
  public String deleteWishlist(Integer wishlistId) {
    Wishlist wishlist = getWishlistByIdAndUserId(wishlistId, userService.userId());
    wishlistItemRepository.deleteByWishlist(wishlist);
    wishlistRepository.delete(wishlist);
    return "Wishlist with id " + wishlistId + " deleted";
  }

  @Override
  public WishlistResponse updateWishlist(Integer wishlistId, WishlistRequest request) {
    Wishlist wishlist = getWishlistByIdAndUserId(wishlistId, userService.userId());
    wishlist.setName(request.getName());
    wishlist.setDescription(request.getDescription());
    wishlist.setPrivacy(request.getPrivacy());
    wishlist = wishlistRepository.save(wishlist);
    return buildWishlistResponse(wishlist);
  }

  @Override
  public WishlistResponse getWishlist(Integer wishlistId) {
    Wishlist wishlist = getWishlistById(wishlistId);
    if (wishlist.getPrivacy().equals(Privacy.PRIVATE)
        && (userService.isAnonymousUser()
            || !wishlist.getUser().getId().equals(userService.userId()))) {
      throw new EntityNotFoundException("Wishlist with id " + wishlistId + " not found");
    }
    return buildWishlistResponse(wishlist);
  }

  public Wishlist getWishlistById(Integer wishlistId) {
    return wishlistRepository
        .findById(wishlistId)
        .orElseThrow(
            () -> new EntityNotFoundException("Wishlist with id " + wishlistId + " not found"));
  }

  @Override
  public List<WishlistListing> getWishlists() {
    return wishlistRepository.findAllByUserId(userService.userId());
  }
}
