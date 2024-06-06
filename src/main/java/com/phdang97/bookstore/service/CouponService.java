package com.phdang97.bookstore.service;

import com.phdang97.bookstore.dto.request.CouponRequest;
import com.phdang97.bookstore.dto.response.CouponResponse;
import com.phdang97.bookstore.entity.Coupon;
import java.util.List;

public interface CouponService {
  Coupon addCoupon(CouponRequest request);

  Coupon updateCoupon(Integer id, CouponRequest request);

  Coupon changeCouponStatus(Integer id, Boolean active);

  String deleteCoupon(Integer id);

  Coupon getCouponById(Integer id);

  List<CouponResponse> getAllCoupons(Boolean active);

  Double calculateDiscount(Coupon coupon, Double subTotal, Double shippingPrice);

  Coupon getCouponByCode(String code);

  void validateCoupon(Coupon coupon, Double subTotal);
}
