package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
  @Query(
      value =
          """
        select c from Cart c
        where c.user.id=:userId
        """)
  Optional<Cart> getCartByUser(Integer userId);
}
