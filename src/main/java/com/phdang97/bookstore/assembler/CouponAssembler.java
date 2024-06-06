package com.phdang97.bookstore.assembler;

import com.phdang97.bookstore.dto.response.CouponResponse;
import com.phdang97.bookstore.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponAssembler {
  public CouponResponse toCouponResponse(Coupon book) {
    return CouponResponse.builder()
        .id(book.getId())
        .code(book.getCode())
        .type(book.getType())
        .discount(book.getDiscount())
        .minimumAmount(book.getMinimumAmount())
        .active(book.getActive())
        .description(book.getDescription())
        .startDate(book.getStartDate())
        .expiryDate(book.getExpiryDate())
        .build();
  }
}
