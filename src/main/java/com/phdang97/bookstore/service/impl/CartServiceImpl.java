package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.dto.ItemDTO;
import com.phdang97.bookstore.dto.request.AddItemRequest;
import com.phdang97.bookstore.dto.response.CartPriceResponse;
import com.phdang97.bookstore.dto.response.CartResponse;
import com.phdang97.bookstore.entity.*;
import com.phdang97.bookstore.enums.CartStatus;
import com.phdang97.bookstore.enums.Government;
import com.phdang97.bookstore.exception.CouponException;
import com.phdang97.bookstore.repository.CartItemRepository;
import com.phdang97.bookstore.repository.CartRepository;
import com.phdang97.bookstore.service.AddressService;
import com.phdang97.bookstore.service.BookService;
import com.phdang97.bookstore.service.CartService;
import com.phdang97.bookstore.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CartServiceImpl implements CartService {

  @Autowired private final CartRepository cartRepository;

  @Autowired private final UserService userService;

  @Autowired private final BookService bookService;

  @Autowired private final AddressService addressService;

  @Autowired private final CouponService couponService;

  @Autowired private final CartItemRepository cartItemRepository;

  @Override
  public Cart createCart(Cart cart, User user) {
    if (cart == null) {
      cart =
          Cart.builder()
              .cartStatus(CartStatus.READ_WRITE)
              .user(user)
              .discountPrice(0.0)
              .itemsPrice(0.0)
              .shippingPrice(0.0)
              .totalItems(0)
              .build();
    }
    return cart;
  }

  @Override
  @Transactional
  public CartResponse addItem(AddItemRequest request) {
    Cart cart = getCartByUserId(userService.userId());
    Book book = bookService.getBookById(request.getBookId());
    bookService.validateBookAvailability(
        request.getBookId(), request.getQuantity(), book.getTitle());
    if (cartItemRepository.existsByCartAndBook(cart, book)) {
      cartItemRepository.updateCartItemQuantity(cart, book, request.getQuantity());
    } else {
      cartItemRepository.save(
          CartItem.builder().cart(cart).book(book).quantity(request.getQuantity()).build());
    }
    CartResponse cartResponse = buildCartResponse(cart);
    cartRepository.save(cart);
    return cartResponse;
  }

  public Cart getCartByUserId(Integer userId) {
    return cartRepository
        .getCartByUser(userId)
        .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
  }

  public CartResponse buildCartResponse(Cart cart) {
    List<ItemDTO> items = getItemDTOS(cart);

    Double cartSubTotal = items.stream().mapToDouble(ItemDTO::getSubTotal).sum();

    cart.setTotalItems(items.size());
    cart.setItemsPrice(cartSubTotal);

    /* get the shipping price*/
    if (cart.getShippingPrice() == 0) {
      cart.setShippingPrice(getShippingPrice(cart));
    }
    Double shippingPrice = cart.getShippingPrice();

    /* get the total*/
    Double total = cartSubTotal + shippingPrice;

    /* get the total after discount*/
    Double totalAfterDiscount;
    Coupon coupon = cart.getCoupon();
    if (coupon != null) {
      totalAfterDiscount =
          couponService.calculateDiscount(cart.getCoupon(), cartSubTotal, shippingPrice);
    } else {
      totalAfterDiscount = total;
    }
    cart.setDiscountPrice(total - totalAfterDiscount);

    return CartResponse.builder()
        .items(items)
        .totalItems(cart.getTotalItems())
        .cartSubTotal(cartSubTotal)
        .cartShippingPrice(shippingPrice)
        .total(total)
        .couponCode(coupon != null ? coupon.getCode() : null)
        .totalAfterDiscount(totalAfterDiscount)
        .build();
  }

  @NotNull
  private List<ItemDTO> getItemDTOS(Cart cart) {
    List<ItemDTO> items = cartItemRepository.getCartItems(cart);
    items =
        items.stream()
            .peek(item -> item.setSubTotal(item.getQuantity() * item.getActualPrice()))
            .toList();
    return items;
  }

  public Double getShippingPrice(Cart cart) {
    if (cart.getShippingPrice() == 0) {
      Address address = addressService.getDefaultAddress();
      if (address != null) {
        return address.getShippingPrice();
      } else {
        return Government.CAIRO.getShippingPrice();
      }
    }
    return cart.getShippingPrice();
  }

  @Override
  @Transactional
  public CartResponse removeItem(Integer itemId) {
    Cart cart = getCartByUserId(userService.userId());
    if (cartItemRepository.existsByCartAndId(cart, itemId)) {
      cartItemRepository.deleteById(itemId);
    } else {
      throw new IllegalArgumentException("Item not found");
    }
    CartResponse cartResponse = buildCartResponse(cart);
    cartRepository.save(cart);
    return cartResponse;
  }

  @Override
  @Transactional
  public CartPriceResponse applyCoupon(String couponCode) {
    Cart cart = getCartByUserId(userService.userId());
    validateCartNotEmpty(cart);
    if (cart.getCoupon() != null) {
      throw new CouponException("Coupon already applied");
    }
    Coupon coupon = couponService.getCouponByCode(couponCode);
    Double itemsPrice = cart.getItemsPrice();
    Double shippingPrice = cart.getShippingPrice();
    Double oldTotal = itemsPrice + shippingPrice;
    couponService.validateCoupon(coupon, itemsPrice);
    Double newTotal = couponService.calculateDiscount(coupon, itemsPrice, shippingPrice);
    cart.setDiscountPrice(oldTotal - newTotal);
    cart.setCoupon(coupon);
    cartRepository.save(cart);
    return CartPriceResponse.builder().oldPrice(oldTotal).newPrice(newTotal).build();
  }

  public void validateCartNotEmpty(Cart cart) {
    if (cartItemRepository.countCartItems(cart) == 0) {
      throw new IllegalArgumentException("Cart is empty");
    }
  }

  @Override
  @Transactional
  public CartPriceResponse updateShipping(Government government) {
    Cart cart = getCartByUserId(userService.userId());
    validateCartNotEmpty(cart);
    Double oldShippingPrice = cart.getShippingPrice();
    Double itemsPrice = cart.getItemsPrice();
    Double discount = cart.getDiscountPrice();
    cart.setShippingPrice(government.getShippingPrice());
    cartRepository.save(cart);
    Double newTotal = itemsPrice + government.getShippingPrice() - discount;
    return CartPriceResponse.builder()
        .oldPrice(itemsPrice + oldShippingPrice - discount)
        .newPrice(newTotal)
        .build();
  }

  @Override
  public CartPriceResponse removeCoupon() {
    Cart cart = getCartByUserId(userService.userId());
    validateCartNotEmpty(cart);
    if (cart.getCoupon() == null) {
      throw new CouponException("No coupon to remove");
    }
    cart.setCoupon(null);
    Double itemsPrice = cart.getItemsPrice();
    Double shippingPrice = cart.getShippingPrice();
    Double discount = cart.getDiscountPrice();
    Double oldTotal = itemsPrice + shippingPrice - discount;
    cart.setDiscountPrice(0.0);
    cartRepository.save(cart);
    return CartPriceResponse.builder()
        .oldPrice(oldTotal)
        .newPrice(itemsPrice + shippingPrice)
        .build();
  }

  @Override
  @Transactional
  public String clearCart() {
    Cart cart = getCartByUserId(userService.userId());
    cartItemRepository.clearCart(cart);
    cart.clearCart();
    cartRepository.save(cart);
    return "Cart cleared";
  }

  @Override
  public CartResponse getCart() {
    Cart cart = getCartByUserId(userService.userId());
    return buildCartResponse(cart);
  }
}
