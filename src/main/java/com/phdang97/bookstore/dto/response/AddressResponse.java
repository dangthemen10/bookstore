package com.phdang97.bookstore.dto.response;

import com.phdang97.bookstore.entity.User;
import com.phdang97.bookstore.enums.Government;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressResponse {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String fullName;

  private String mobileNumber;

  private Government government;

  private String city;

  private String fullAddress;

  private Boolean isDefault;

  private User user;
}
