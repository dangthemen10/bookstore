package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.dto.ItemDTO;
import com.phdang97.bookstore.entity.Book;
import com.phdang97.bookstore.entity.Wishlist;
import com.phdang97.bookstore.entity.WishlistItem;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Integer> {
  @Query(
      value =
          """
        select new com.phdang97.bookstore.dto.ItemDTO
        (wi.id, b.id, b.title, b.actualPrice, wi.quantity ,b.bookThumbnail)
        from WishlistItem wi inner join wi.book b
        where wi.wishlist=:wishlist
        """)
  List<ItemDTO> getWishlistItems(@Param("wishlist") Wishlist wishlist);

  @Modifying
  @Query(
      value =
          """
        update WishlistItem wi
        set wi.quantity=:quantity
        where wi.wishlist=:wishlist and wi.book=:book
        """)
  @Transactional
  void updateWishlistItemQuantity(
      @Param("wishlist") Wishlist wishlist,
      @Param("book") Book book,
      @Param("quantity") Integer quantity);

  boolean existsByWishlistAndBook(Wishlist wishlist, Book book);

  boolean existsByWishlistAndId(Wishlist wishlist, Integer id);

  @Modifying
  @Transactional
  void deleteByWishlist(Wishlist wishlist);
}
