package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.dto.request.CouponRequest;
import com.phdang97.bookstore.dto.response.CouponResponse;
import com.phdang97.bookstore.entity.Coupon;
import com.phdang97.bookstore.exception.CouponException;
import com.phdang97.bookstore.repository.CouponRepository;
import com.phdang97.bookstore.service.CouponService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {
  @Autowired private final CouponRepository couponRepository;

  @Autowired private final ModelMapper modelMapper;

  @Override
  public Coupon addCoupon(CouponRequest request) {
    validateCouponDuplicate(request.getCode());
    Coupon coupon = modelMapper.map(request, Coupon.class);
    coupon.setActive(true);
    couponRepository.save(coupon);
    return coupon;
  }

  @Override
  public Coupon updateCoupon(Integer id, CouponRequest request) {
    Coupon coupon = getCouponById(id);
    modelMapper.map(request, coupon);
    couponRepository.save(coupon);
    return coupon;
  }

  @Override
  public Coupon changeCouponStatus(Integer id, Boolean active) {
    Coupon coupon = getCouponById(id);
    coupon.setActive(active);
    couponRepository.save(coupon);
    return coupon;
  }

  @Override
  public String deleteCoupon(Integer id) {
    Coupon coupon = getCouponById(id);
    couponRepository.delete(coupon);
    return String.format("A coupon with id %d has been deleted!", id);
  }

  @Override
  public Coupon getCouponById(Integer id) {
    return couponRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format("coupon with id %s not found", id)));
  }

  @Override
  public List<CouponResponse> getAllCoupons(Boolean active) {
    return couponRepository.findAllCoupons(active);
  }

  public void validateCouponDuplicate(String code) {
    Optional<Coupon> coupon = couponRepository.findCouponByCode(code);
    if (coupon.isPresent()) {
      throw new EntityExistsException(String.format("coupon with code %s is already found", code));
    }
  }

  @Override
  public Double calculateDiscount(Coupon coupon, Double subTotal, Double shippingPrice) {
    return switch (coupon.getType()) {
      case FREE_SHIPPING -> subTotal;
      case PERCENTAGE -> subTotal - (subTotal * coupon.getDiscount() / 100) + shippingPrice;
      case FIXED_AMOUNT -> subTotal - coupon.getDiscount() + shippingPrice;
    };
  }

  @Override
  public Coupon getCouponByCode(String code) {
    return couponRepository
        .findCouponByCode(code)
        .orElseThrow(
            () ->
                new EntityNotFoundException(String.format("coupon with code %s not found", code)));
  }

  @Override
  public void validateCoupon(Coupon coupon, Double subTotal) {
    if (!coupon.isCouponValid()) {
      throw new CouponException(String.format("coupon with code %s is expired", coupon.getCode()));
    } else if (coupon.getMinimumAmount() > subTotal) {
      throw new CouponException(
          String.format("coupon with code %s is not valid for this amount", coupon.getCode()));
    }
  }
}
