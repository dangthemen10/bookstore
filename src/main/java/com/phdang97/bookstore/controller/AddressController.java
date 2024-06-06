package com.phdang97.bookstore.controller;

import com.phdang97.bookstore.assembler.AddressAssembler;
import com.phdang97.bookstore.dto.request.AddressRequest;
import com.phdang97.bookstore.dto.response.AddressResponse;
import com.phdang97.bookstore.entity.Address;
import com.phdang97.bookstore.enums.Government;
import com.phdang97.bookstore.service.AddressService;
import com.phdang97.bookstore.service.impl.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/addresses")
public class AddressController {
  @Autowired private final AddressService addressService;

  @Autowired private final UserService userService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public AddressResponse addAddress(@RequestBody @Valid AddressRequest addressRequest) {
    Address address = addressService.addAddress(addressRequest);
    return new AddressAssembler().toAddressResponse(address);
  }

  @PutMapping("/{id}")
  public AddressResponse updateAddress(
      @PathVariable(name = "id") @PositiveOrZero Integer addressId,
      @RequestBody @Valid AddressRequest addressRequest) {
    Address address = addressService.updateAddress(addressId, addressRequest);
    return new AddressAssembler().toAddressResponse(address);
  }

  @DeleteMapping("/{id}")
  public String deleteAddress(@PathVariable(name = "id") @PositiveOrZero Integer addressId) {
    return addressService.deleteAddress(addressId);
  }

  @GetMapping("/{id}")
  public AddressResponse getAddress(@PathVariable(name = "id") @PositiveOrZero Integer addressId) {
    Address address = addressService.getAddressByIdAndUserId(addressId, userService.userId());
    return new AddressAssembler().toAddressResponse(address);
  }

  @GetMapping("/default")
  public AddressResponse getDefaultAddress() {
    Address address = addressService.getDefaultAddress();
    return new AddressAssembler().toAddressResponse(address);
  }

  @GetMapping
  public Set<AddressResponse> getAllAddresses() {
    Set<Address> addresses = addressService.getAllAddresses();
    return addresses.stream()
        .map(address -> new AddressAssembler().toAddressResponse(address))
        .collect(Collectors.toSet());
  }

  @PatchMapping("/{id}/default")
  public AddressResponse setAddressAsDefault(
      @PathVariable(name = "id") @PositiveOrZero Integer addressId) {
    Address address = addressService.setAddressAsDefault(addressId);
    return new AddressAssembler().toAddressResponse(address);
  }

  @GetMapping("/governments")
  public List<String> getGovernments() {
    return Government.getGovernments();
  }
}
