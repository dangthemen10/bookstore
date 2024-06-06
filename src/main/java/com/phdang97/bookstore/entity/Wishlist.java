package com.phdang97.bookstore.entity;

import com.phdang97.bookstore.enums.Privacy;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "wishlist")
public class Wishlist extends BaseEntity {
  @OneToMany(
      mappedBy = "wishlist",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private final Set<WishlistItem> wishlistItems = new HashSet<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String description;
  @Enumerated(EnumType.STRING)
  private Privacy privacy;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
