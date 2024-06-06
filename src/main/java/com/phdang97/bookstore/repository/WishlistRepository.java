package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.dto.response.WishlistListing;
import com.phdang97.bookstore.entity.Wishlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
  @Query(
      value =
          """
            SELECT CASE WHEN COUNT(w) > 0 THEN TRUE ELSE FALSE END
            FROM Wishlist w
            WHERE w.name = :name
            AND w.user.id = :userId
            """)
  boolean existsByNameAndUserId(String name, Integer userId);

  @Query(
      value =
          """
            SELECT w
            FROM Wishlist w
            WHERE w.id = :wishlistId
            AND w.user.id = :userId
            """)
  Optional<Wishlist> findByIdAndUserId(Integer wishlistId, Integer userId);

  @Query(
      value =
          """
            select new com.phdang97.bookstore.dto.response.WishlistListing(w.id, w.name, w.creationTime, w.privacy)
            from Wishlist w
            where w.user.id =:userId
            """)
  List<WishlistListing> findAllByUserId(Integer userId);
}
