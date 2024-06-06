package com.phdang97.bookstore.controller;

import com.phdang97.bookstore.assembler.CouponAssembler;
import com.phdang97.bookstore.dto.request.CouponRequest;
import com.phdang97.bookstore.dto.response.CouponResponse;
import com.phdang97.bookstore.entity.Coupon;
import com.phdang97.bookstore.enums.CouponType;
import com.phdang97.bookstore.service.CouponService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
  @Autowired private final CouponService couponService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public CouponResponse addCoupon(@RequestBody CouponRequest request) {
    Coupon coupon = couponService.addCoupon(request);
    return new CouponAssembler().toCouponResponse(coupon);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{id}")
  public CouponResponse updateCoupon(
      @PathVariable(name = "id") Integer id, @RequestBody CouponRequest request) {
    Coupon coupon = couponService.updateCoupon(id, request);
    return new CouponAssembler().toCouponResponse(coupon);
  }

  @PatchMapping("/{id}")
  public CouponResponse changeCouponStatus(
      @PathVariable(name = "id") Integer id, @RequestParam(name = "active") Boolean active) {
    Coupon coupon = couponService.changeCouponStatus(id, active);
    return new CouponAssembler().toCouponResponse(coupon);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{id}")
  public String deleteCoupon(@PathVariable(name = "id") @Positive Integer id) {
    return couponService.deleteCoupon(id);
  }

  @GetMapping("/{id}")
  public CouponResponse getCouponById(@PathVariable(name = "id") @Positive Integer id) {
    Coupon coupon = couponService.getCouponById(id);
    return new CouponAssembler().toCouponResponse(coupon);
  }

  @GetMapping
  public List<CouponResponse> getAllCoupons(
      @RequestParam(name = "active", required = false) Boolean active) {
    return couponService.getAllCoupons(active);
  }

  @GetMapping("/types")
  public List<String> getCouponsType(){
    return CouponType.getCouponTypes();
  }
}
