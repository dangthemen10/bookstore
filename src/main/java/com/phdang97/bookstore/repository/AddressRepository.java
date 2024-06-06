package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.entity.Address;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
  Optional<Address> findAddressByIdAndUserId(Integer addressId, Integer userId);

  Set<Address> getAddressByUserId(Integer userId);

  Optional<Address> findAddressByUserIdAndIsDefault(Integer userId, Boolean isDefault);
}
