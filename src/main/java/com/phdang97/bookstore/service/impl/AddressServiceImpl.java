package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.dto.request.AddressRequest;
import com.phdang97.bookstore.entity.Address;
import com.phdang97.bookstore.entity.User;
import com.phdang97.bookstore.repository.AddressRepository;
import com.phdang97.bookstore.service.AddressService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
  @Autowired private final AddressRepository addressRepository;

  @Autowired private final UserService userService;

  @Autowired private final ModelMapper modelMapper;

  @Override
  @Transactional
  public Address addAddress(AddressRequest request) {
    Integer userId = userService.userId();
    User user = userService.getUserById(userId);
    Set<Address> addresses = user.getAddresses();
    Address address = modelMapper.map(request, Address.class);
    if (addresses.isEmpty()) {
      address.setIsDefault(true);
      address.setUser(user);
    } else if (addresses.contains(address)) {
      throw new EntityExistsException("the address you are trying to add is already exists");
    } else {
      address.setIsDefault(false);
      address.setUser(user);
    }
    addressRepository.save(address);
    return address;
  }

  @Override
  public Address updateAddress(Integer addressId, AddressRequest request) {
    Integer userId = userService.userId();
    User user = userService.getUserById(userId);
    Set<Address> addresses = user.getAddresses();
    Address requiredAddress = modelMapper.map(request, Address.class);
    Address targetedAddress = getAddressByIdAndUserId(addressId, userId);
    if (addresses.contains(requiredAddress)) {
      throw new EntityExistsException("the address you are trying to add is already exists");
    }
    modelMapper.map(request, targetedAddress);
    addressRepository.save(targetedAddress);
    return targetedAddress;
  }

  @Override
  public String deleteAddress(Integer addressId) {
    Integer userId = userService.userId();
    Address targetedAddress = getAddressByIdAndUserId(addressId, userId);
    addressRepository.delete(targetedAddress);
    return String.format("A address with id %d has been deleted!", addressId);
  }

  @Override
  public Address getAddressByIdAndUserId(Integer addressId, Integer userId) {
    return addressRepository
        .findAddressByIdAndUserId(addressId, userId)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    String.format("Address with %d is not found", addressId)));
  }

  @Override
  public Address getDefaultAddress() {
    Integer userId = userService.userId();
    return addressRepository.findAddressByUserIdAndIsDefault(userId, true).orElse(null);
  }

  @Override
  public Set<Address> getAllAddresses() {
    Integer userId = userService.userId();
    return addressRepository.getAddressByUserId(userId);
  }

  @Override
  public Address setAddressAsDefault(Integer addressId) {
    Integer userId = userService.userId();
    User user = userService.getUserById(userId);
    Set<Address> addresses = user.getAddresses();
    Address targetedAddress = getAddressByIdAndUserId(addressId, userId);
    addresses.stream()
        .filter(Address::getIsDefault)
        .forEach(
            address -> {
              address.setIsDefault(false);
              addressRepository.save(address);
            });
    targetedAddress.setIsDefault(true);
    addressRepository.save(targetedAddress);
    return targetedAddress;
  }
}
