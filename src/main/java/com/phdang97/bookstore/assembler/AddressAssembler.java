package com.phdang97.bookstore.assembler;

import com.phdang97.bookstore.dto.response.AddressResponse;
import com.phdang97.bookstore.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressAssembler {

  public AddressResponse toAddressResponse(Address address) {
    return AddressResponse.builder()
        .id(address.getId())
        .fullName(address.getFullName())
        .mobileNumber(address.getMobileNumber())
        .government(address.getGovernment())
        .city(address.getCity())
        .fullAddress(address.getFullAddress())
        .isDefault(address.getIsDefault())
        .user(address.getUser())
        .build();
  }
}
