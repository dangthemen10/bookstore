package com.phdang97.bookstore.service;

import com.phdang97.bookstore.dto.request.AddItemRequest;
import com.phdang97.bookstore.dto.response.CartPriceResponse;
import com.phdang97.bookstore.dto.response.CartResponse;
import com.phdang97.bookstore.entity.Cart;
import com.phdang97.bookstore.entity.User;
import com.phdang97.bookstore.enums.Government;

public interface CartService {
  Cart createCart(Cart cart, User user);

  CartResponse addItem(AddItemRequest request);

  CartResponse removeItem(Integer itemId);

  CartPriceResponse applyCoupon(String couponCode);

  CartPriceResponse updateShipping(Government government);

  CartPriceResponse removeCoupon();

  String clearCart();

  CartResponse getCart();
}
