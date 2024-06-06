package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.dto.response.CouponResponse;
import com.phdang97.bookstore.entity.Coupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
  Optional<Coupon> findCouponByCode(String code);

  @Query(
      value =
          """
            select new com.phdang97.bookstore.dto.response.CouponResponse(c.id, c.code, c.type, c.discount, c.minimumAmount, c.active, c.description, c.expiryDate, c.expiryDate)
            from Coupon c
            where (:active is null or c.active=:active)
            """)
  List<CouponResponse> findAllCoupons(Boolean active);
}
