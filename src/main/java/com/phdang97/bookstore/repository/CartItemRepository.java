package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.dto.ItemDTO;
import com.phdang97.bookstore.entity.Book;
import com.phdang97.bookstore.entity.Cart;
import com.phdang97.bookstore.entity.CartItem;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
  boolean existsByCartAndBook(Cart cart, Book book);

  boolean existsByCartAndId(Cart cart, Integer itemId);

  @Query(
      value =
          """
        select new com.phdang97.bookstore.dto.ItemDTO
        (ci.id, b.id, b.title, b.actualPrice, ci.quantity ,b.bookThumbnail)
        from CartItem ci inner join ci.book b
        where ci.cart=:cart
        """)
  List<ItemDTO> getCartItems(@Param("cart") Cart cart);

  @Modifying
  @Query(
      value =
          """
        update CartItem c
        set c.quantity=:quantity
        where c.cart=:cart and c.book=:book
        """)
  @Transactional
  void updateCartItemQuantity(
      @Param("cart") Cart cart, @Param("book") Book book, @Param("quantity") Integer quantity);

  @Query(
      value =
          """
        select count(c) from CartItem c
        where c.cart=:cart""")
  Integer countCartItems(@Param("cart") Cart cart);

  @Transactional
  @Modifying
  @Query(
      value =
          """
        delete from CartItem c
        where c.cart=:cart
        """)
  void clearCart(@Param("cart") Cart cart);
}
