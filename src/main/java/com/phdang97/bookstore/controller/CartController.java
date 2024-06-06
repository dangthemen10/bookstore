package com.phdang97.bookstore.controller;

import com.phdang97.bookstore.dto.request.AddItemRequest;
import com.phdang97.bookstore.dto.response.CartPriceResponse;
import com.phdang97.bookstore.dto.response.CartResponse;
import com.phdang97.bookstore.enums.Government;
import com.phdang97.bookstore.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
  @Autowired private final CartService cartService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public CartResponse addItem(@RequestBody @Valid AddItemRequest request) {
    return cartService.addItem(request);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{itemId}")
  public CartResponse removeItem(@PathVariable @Positive Integer itemId) {
    return cartService.removeItem(itemId);
  }

  @PatchMapping("/coupon")
  public CartPriceResponse applyCoupon(@RequestParam("code") @NotBlank String couponCode) {
    return cartService.applyCoupon(couponCode);
  }

  @PatchMapping("/shipping")
  public CartPriceResponse updateShipping(@RequestParam("government") @NotBlank String government) {
    return cartService.updateShipping(Government.fromValue(government));
  }

  @DeleteMapping("/coupon")
  public CartPriceResponse removeCoupon() {
    return cartService.removeCoupon();
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/clear")
  public String clearCart() {
    return cartService.clearCart();
  }

  @GetMapping
  public CartResponse getCart() {
    return cartService.getCart();
  }
}
