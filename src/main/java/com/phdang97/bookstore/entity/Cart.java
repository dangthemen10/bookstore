package com.phdang97.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.phdang97.bookstore.enums.CartStatus;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@EqualsAndHashCode(
    callSuper = true,
    exclude = {"cartItems"})
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "carts")
public class Cart extends BaseEntity {
  @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonManagedReference
  private final Set<CartItem> cartItems = new HashSet<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "total_items")
  private Integer totalItems;
  @Column(name = "shipping_price")
  private Double shippingPrice;
  @Column(name = "items_price")
  private Double itemsPrice;
  @Column(name = "discount_price")
  private Double discountPrice;
  @Enumerated(EnumType.STRING)
  @Column(name = "cart_status")
  private CartStatus cartStatus;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  public void clearCart() {
    this.setTotalItems(0);
    this.setDiscountPrice(0.0);
    this.setShippingPrice(0.0);
    this.setItemsPrice(0.0);
    this.setCoupon(null);
  }
}
