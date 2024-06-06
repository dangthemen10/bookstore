package com.phdang97.bookstore.dto.response;

import com.phdang97.bookstore.enums.CouponType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {
  private Integer id;

  private String code;

  private CouponType type;

  private Integer discount;

  private Integer minimumAmount;

  private Boolean active;

  private String description;

  private Date startDate;

  private Date expiryDate;
}
