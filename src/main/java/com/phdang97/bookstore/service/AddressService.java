package com.phdang97.bookstore.service;

import com.phdang97.bookstore.dto.request.AddressRequest;
import com.phdang97.bookstore.entity.Address;
import java.util.Set;

public interface AddressService {
  Address addAddress(AddressRequest request);

  Address updateAddress(Integer addressId, AddressRequest request);

  String deleteAddress(Integer addressId);

  Address getAddressByIdAndUserId(Integer addressId, Integer userId);

  Address getDefaultAddress();

  Set<Address> getAllAddresses();

  Address setAddressAsDefault(Integer addressId);
}
